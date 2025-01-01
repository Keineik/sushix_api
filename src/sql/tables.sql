/*
 * HOW TO RUN THIS SCRIPT:
 *
 * 1. Enable full-text search on your SQL Server instance.
 *
 * 2. Open the script inside SQL Server Management Studio and enable SQLCMD mode.
 *    This option is in the Query menu.
 *
 * 3. Set the following environment variable to your own data path.
 */
:setvar DataPath "G:\Github\sushix_api\src\sql\data\"

:setvar DatabaseName "SushiX"
GO

-- Check for SQLCMD mode by checking a condition that evaluates to false in SQLCMD mode
IF ('$(DatabaseName)' = '$' + '(DatabaseName)')
    RAISERROR ('This script must be run in SQLCMD mode. Disconnecting.', 20, 1) WITH LOG
GO
-- The below is only run if SQLCMD is off, or the user lacks permission to raise fatal errors
IF @@ERROR != 0
    SET NOEXEC ON
GO

IF '$(DataPath)' IS NULL OR '$(DataPath)' = ''
BEGIN
	RAISERROR(N'The variable SqlSamplesSourceDataPath must be defined.', 16, 127) WITH NOWAIT
	RETURN
END;

USE [master];
GO
-- ****************************************
-- Drop Database
-- ****************************************
PRINT '';
PRINT '*** Dropping Database';
GO

IF EXISTS (SELECT [name] FROM [master].[sys].[databases] WHERE [name] = N'$(DatabaseName)')
BEGIN
	ALTER DATABASE $(DatabaseName) SET SINGLE_USER WITH ROLLBACK IMMEDIATE
    DROP DATABASE $(DatabaseName);
END

-- If the database has any other open connections close the network connection.
IF @@ERROR = 3702
    RAISERROR('$(DatabaseName) database cannot be dropped because there are still other open connections', 127, 127) WITH NOWAIT, LOG;
GO

-- ****************************************
-- Create Database
-- ****************************************
PRINT '';
PRINT '*** Creating Database';
GO

CREATE DATABASE $(DatabaseName);
GO

PRINT '';
PRINT '*** Checking for $(DatabaseName) Database';
/* CHECK FOR DATABASE IF IT DOESN'T EXISTS, DO NOT RUN THE REST OF THE SCRIPT */
IF NOT EXISTS (SELECT TOP 1 1 FROM sys.databases WHERE name = N'$(DatabaseName)')
BEGIN
PRINT '*******************************************************************************************************************************************************************'
+char(10)+'********$(DatabaseName) Database does not exist.  Make sure that the script is being run in SQLCMD mode and that the variables have been correctly set.*********'
+char(10)+'*******************************************************************************************************************************************************************';
SET NOEXEC ON;
END
GO

USE $(DatabaseName);
GO

-- ****************************************
-- Create Tables
-- ****************************************

CREATE TABLE MenuCategory (
    CategoryID INT IDENTITY(1,1) PRIMARY KEY,
    CategoryName NVARCHAR(100) NOT NULL
);

CREATE TABLE MenuItem (
    ItemID INT IDENTITY(1,1) PRIMARY KEY,
    ItemName NVARCHAR(100) NOT NULL,
    UnitPrice DECIMAL(19,4) CHECK (UnitPrice >= 0),
    ServingUnit NVARCHAR(10),
    CategoryID INT,
	SoldQuantity INT DEFAULT 0,
    IsDiscontinued BIT,
	ImgUrl NVARCHAR(2083),

    CONSTRAINT FK_MenuItem_MenuCategory FOREIGN KEY (CategoryID) REFERENCES MenuCategory(CategoryID) ON DELETE SET NULL
);

CREATE TABLE Branch (
    BranchID INT IDENTITY(1,1) PRIMARY KEY,
    BranchName NVARCHAR(100) NOT NULL,
    BranchAddress NVARCHAR(255) NOT NULL,
    BranchRegion NVARCHAR(50),
    OpeningTime TIME,
    ClosingTime TIME,
    BranchPhoneNumber VARCHAR(25),
    HasCarParking BIT,
    HasBikeParking BIT,
	ImgUrl NVARCHAR(2083)
);

CREATE TABLE Department (
	DeptID INT IDENTITY(1,1) PRIMARY KEY,
	BranchID INT,
    DeptName VARCHAR(10) CHECK (DeptName IN ('Kitchen', 'Reception', 'Waiter', 'Cashier', 'Manager')),
    Salary DECIMAL(19,4),

	CONSTRAINT FK_Department_Branch FOREIGN KEY (BranchID) REFERENCES Branch(BranchID) ON DELETE CASCADE,
	CONSTRAINT UQ_BranchID_DeptName UNIQUE (BranchID, DeptName)
);

CREATE TABLE BranchMenuItem (
    BranchID INT,
    ItemID INT,
    IsShippable BIT,

    CONSTRAINT PK_BranchMenuItem PRIMARY KEY (BranchID, ItemID),
    CONSTRAINT FK_BranchMenuItem_Branch FOREIGN KEY (BranchID) REFERENCES Branch(BranchID),
    CONSTRAINT FK_BranchMenuItem_MenuItem FOREIGN KEY (ItemID) REFERENCES MenuItem(ItemID)
);

CREATE TABLE Staff (
    StaffID INT IDENTITY(1,1) PRIMARY KEY,
    DeptName VARCHAR(10),
    BranchID INT,

    CONSTRAINT FK_Staff_Department FOREIGN KEY (BranchID, DeptName) REFERENCES Department(BranchID, DeptName)
);

CREATE TABLE StaffInfo (
	StaffID INT PRIMARY KEY,
	StaffName NVARCHAR(100) NOT NULL,
	StaffDOB DATE,
	StaffGender CHAR(1) CHECK (StaffGender IN ('M', 'F')),
	StaffPhoneNumber VARCHAR(20) NOT NULL UNIQUE,
	StaffCitizenID VARCHAR(20) NOT NULL UNIQUE,

	CONSTRAINT FK_StaffInfo_Staff FOREIGN KEY (StaffID) REFERENCES Staff(StaffID) ON DELETE CASCADE
)

CREATE TABLE WorkHistory (
	StaffID INT,
	StartDate DATETIME,
	BranchID INT NOT NULL,
	DeptName VARCHAR(10),
	QuitDate DATETIME,

	CONSTRAINT PK_WorkHistory PRIMARY KEY (StaffID, StartDate),
	CONSTRAINT FK_WorkHistory_Staff FOREIGN KEY (StaffID) REFERENCES Staff(StaffID),
	CONSTRAINT FK_WorkHistory_Department FOREIGN KEY (BranchID, DeptName) REFERENCES Department(BranchID, DeptName)
);

CREATE TABLE Customer (
    CustID INT IDENTITY(1,1) PRIMARY KEY,
    CustName VARCHAR(255) NOT NULL,
    CustEmail VARCHAR(255),
    CustGender CHAR(1) CHECK (CustGender IN ('M', 'F')),
    CustPhoneNumber VARCHAR(20),
    CustCitizenID VARCHAR(20)
);
-- Because null is allowed but if not null, it must be unique
CREATE UNIQUE INDEX UQ_Customer_CustPhoneNumber ON Customer(CustPhoneNumber) WHERE CustPhoneNumber IS NOT NULL;
CREATE UNIQUE INDEX UQ_Customer_CustCitizenID ON Customer(CustCitizenID) WHERE CustCitizenID IS NOT NULL;

CREATE TABLE CardType (
    CardTypeID INT IDENTITY(1,1) PRIMARY KEY,
    CardName VARCHAR(10) NOT NULL,
	DiscountRate DECIMAL(4,3) CHECK (DiscountRate BETWEEN 0 AND 1),
    PointsRequiredForRenewal INT CHECK (PointsRequiredForRenewal >= 0),
    PointsRequiredForUpgrade INT CHECK (PointsRequiredForUpgrade >= 0)
);
	
CREATE TABLE MembershipCard (
    CardID INT IDENTITY(1,1) PRIMARY KEY,
    IssuedDate DATE DEFAULT GETDATE(),
    CardType INT DEFAULT 1,
    CustID INT NOT NULL UNIQUE,
	StaffID INT NOT NULL,
	Points INT DEFAULT 0,
	
	CONSTRAINT FK_MembershipCard_Staff FOREIGN KEY (StaffID) REFERENCES Staff(StaffID),
    CONSTRAINT FK_MembershipCard_Customer FOREIGN KEY (CustID) REFERENCES Customer(CustID),
    CONSTRAINT FK_MembershipCard_CardType FOREIGN KEY (CardType) REFERENCES CardType(CardTypeID)
);

CREATE TABLE Account (
    AccountID INT IDENTITY(1,1) PRIMARY KEY,
    Username NVARCHAR(100) NOT NULL UNIQUE,
    [Password] NVARCHAR(255) NOT NULL,
    CustID INT DEFAULT NULL,
    StaffID INT DEFAULT NULL,
	IsAdmin BIT NOT NULL DEFAULT 0,

    CONSTRAINT FK_User_Customer FOREIGN KEY (CustID) REFERENCES Customer(CustID),
    CONSTRAINT FK_User_Staff FOREIGN KEY (StaffID) REFERENCES Staff(StaffID)
);

CREATE TABLE OnlineAccess (
	AccessID INT IDENTITY(1,1) PRIMARY KEY,
	CustID INT NOT NULL,
	StartDateTime DATETIME NOT NULL,
	EndDateTime DATETIME,

	CONSTRAINT UQ_CustID_StartDateTime UNIQUE (CustID, StartDateTime),
	CONSTRAINT FK_OnlineAccess FOREIGN KEY (CustID) REFERENCES Customer(CustID)
);

CREATE TABLE Reservation (
	RsID INT IDENTITY(1, 1) PRIMARY KEY,
	NumOfGuests INT NOT NULL CHECK (NumOfGuests >= 0),
	RsDateTime DATETIME DEFAULT GETDATE(),
	ArrivalDateTime DATETIME NOT NULL,	
	RsNotes NVARCHAR(2047),
	BranchID INT NOT NULL,
	CustID INT NOT NULL,
	RsStatus CHAR(15) CHECK (RsStatus IN (
		'Not Confirmed', 'Cancelled', 'Confirmed'
	)) DEFAULT 'Not Confirmed',

	CONSTRAINT FK_Reservation_Branch FOREIGN KEY (BranchID) REFERENCES Branch(BranchID),
	CONSTRAINT FK_Reservation_Customer FOREIGN KEY (CustID) REFERENCES Customer(CustID)
);

CREATE TABLE [Order] (
	OrderID INT IDENTITY(1, 1),
	OrderDateTime DATETIME DEFAULT GETDATE(),
	OrderStatus CHAR(10) CHECK (OrderStatus IN (
		'UNVERIFIED', 'VERIFIED', 'DELIVERED', 'COMPLETED', 'CANCELLED'
	)),
	StaffID INT,
	CustID INT NOT NULL,
	BranchID INT NOT NULL,
	OrderType CHAR(1) CHECK (OrderType IN (
		'D', 'I' -- D for DeliveryOrder, I for DineInOrder
	)),

	CONSTRAINT PK_Order PRIMARY KEY NONCLUSTERED (OrderID),
	CONSTRAINT FK_Order_Customer FOREIGN KEY (CustID) REFERENCES Customer(CustID),
	CONSTRAINT FK_Order_Branch FOREIGN KEY (BranchID) REFERENCES Branch(BranchID)
);

CREATE TABLE OrderDetails (
	DetailsID INT IDENTITY(1, 1) PRIMARY KEY,
	OrderID INT NOT NULL,
	ItemID INT NOT NULL,
	UnitPrice DECIMAL(19,4) CHECK (UnitPrice >= 0),
	Quantity INT CHECK (Quantity > 0),

	CONSTRAINT UQ_OrderID_ItemID UNIQUE (OrderID, ItemID),
	CONSTRAINT FK_OrderDetails_Order FOREIGN KEY (OrderID) REFERENCES [Order](OrderID) ON DELETE CASCADE,
	CONSTRAINT FK_OrderDetails_Item FOREIGN KEY (ItemID) REFERENCES MenuItem(ItemID),
);

CREATE TABLE [Table] (
	TableID INT IDENTITY(1,1) PRIMARY KEY,
	TableCode INT NOT NULL,
	BranchID INT NOT NULL,
	NumOfSeats INT,
	isVacant BIT DEFAULT 1,

	CONSTRAINT UQ_Table UNIQUE (BranchID, TableCode),
	CONSTRAINT FK_Table_Branch FOREIGN KEY (BranchID) REFERENCES Branch(BranchID),
);

CREATE TABLE DineInOrder (
	OrderID INT PRIMARY KEY,
	TableCode INT,
	BranchID INT NOT NULL,
	RsID INT,

	CONSTRAINT FK_DineInOrder_Order FOREIGN KEY (OrderID) REFERENCES [Order](OrderID) ON DELETE CASCADE,
	CONSTRAINT FK_DineInOrder_Table FOREIGN KEY (BranchID, TableCode) REFERENCES [Table](BranchID, TableCode),
	CONSTRAINT FK_DineInOrder_Reservation FOREIGN KEY (RsID) REFERENCES Reservation(RsID) ON DELETE SET NULL
);

CREATE TABLE DeliveryOrder (
	OrderID INT PRIMARY KEY,
	DeliveryAddress NVARCHAR(2047) NOT NULL,
	DeliveryDateTime DATETIME NOT NULL,

	CONSTRAINT FK_DeliveryOrder_Order FOREIGN KEY (OrderID) REFERENCES [Order](OrderID),
);

CREATE TABLE Coupon (
    CouponID INT IDENTITY(1,1) PRIMARY KEY,
    CouponCode VARCHAR(50) NOT NULL UNIQUE,
    CouponDesc NVARCHAR(255),
	DiscountFlat DECIMAL(19,4),
    DiscountRate DECIMAL(4,3) CHECK (DiscountRate BETWEEN 0 AND 1),
    MinPurchase DECIMAL(19,4) NOT NULL,
    MaxDiscount DECIMAL(19,4) NOT NULL,
    EffectiveDate DATE NOT NULL,
    ExpiryDate DATE NOT NULL,
    TotalUsageLimit INT NOT NULL,
	RemainingUsage INT NOT NULL,
    MinMembershipRequirement INT

		CONSTRAINT FK_Coupon_CardType FOREIGN KEY (MinMembershipRequirement) REFERENCES CardType(CardTypeID),
);

CREATE TABLE Invoice(
	InvoiceID INT IDENTITY(1, 1),
	OrderID INT NOT NULL,
	Subtotal DECIMAL(19,4) NOT NULL,
	DiscountRate DECIMAL(4,3) CHECK (DiscountRate BETWEEN 0 AND 1),
	TaxRate DECIMAL(4,3) CHECK (TaxRate BETWEEN 0 AND 1),
	ShippingCost DECIMAL(19, 4) DEFAULT 0,
	PaymentMethod NVARCHAR(50) NOT NULL,
	InvoiceDate DATETIME DEFAULT GETDATE(),
	CouponID INT,
	BranchID INT NOT NULL,
	CustID INT NOT NULL,

	CONSTRAINT PK_Invoice PRIMARY KEY NONCLUSTERED (InvoiceID),
	CONSTRAINT FK_Invoice_Coupon FOREIGN KEY (CouponID) REFERENCES Coupon(CouponID),
	CONSTRAINT FK_Invoice_Order FOREIGN KEY (OrderID) REFERENCES [Order](OrderID),
	CONSTRAINT FK_Invoice_Branch FOREIGN KEY (BranchID) REFERENCES Branch(BranchID),
	CONSTRAINT FK_Invoice_Customer FOREIGN KEY (CustID) REFERENCES Customer(CustID)
);

CREATE TABLE CustomerRating (
	RatingID INT IDENTITY(1, 1) PRIMARY KEY,
	ServiceRating INT CHECK(ServiceRating >= 1 and ServiceRating <= 10),
	LocationRating INT CHECK(LocationRating >= 1 and LocationRating <= 10),
	FoodRating INT CHECK(FoodRating >= 1 and FoodRating <= 10),
	PricingRating INT CHECK(PricingRating >= 1 and PricingRating <= 10),
	AmbianceRating INT CHECK(AmbianceRating >= 1 and AmbianceRating <= 10),
	FeedbackCmt NVARCHAR(MAX),
	FeedbackDate DATE DEFAULT GETDATE(),
	InvoiceID INT UNIQUE NOT NULL,
	BranchID INT,

	CONSTRAINT FK_CustomerRating_Invoice FOREIGN KEY (InvoiceID) REFERENCES Invoice(InvoiceID),
	CONSTRAINT FK_CustomerRating_Branch FOREIGN KEY (BranchID) REFERENCES Branch(BranchID)
);

CREATE TABLE StaffRating (
	StaffRatingID INT IDENTITY(1,1) PRIMARY KEY,
	RatingID INT,
	StaffID INT,
	StaffRating INT NOT NULL CHECK (StaffRating >= 0 AND StaffRating <= 10),

	CONSTRAINT UQ_RatingID_StaffID UNIQUE(RatingID, StaffID),
	CONSTRAINT FK_StaffRating_CustomerRating FOREIGN KEY (StaffID) REFERENCES Staff(StaffID)
);

-- ******************************************************
-- Load data
-- ******************************************************
PRINT '';
PRINT '*** Loading Data';
GO

-- ******************************************************

PRINT 'Loading MenuCategory';
BULK INSERT MenuCategory FROM '$(DataPath)MenuCategory.csv'
WITH (
    CHECK_CONSTRAINTS,
    CODEPAGE='ACP',
    DATAFILETYPE = 'char',
    FIELDTERMINATOR= ',',
    KEEPIDENTITY,
    TABLOCK,
	FIRSTROW = 2,
	FORMAT = 'CSV'
);

-- ******************************************************

PRINT 'Loading MenuItem';
BULK INSERT MenuItem FROM '$(DataPath)MenuItem.csv'
WITH (
    CHECK_CONSTRAINTS,
    CODEPAGE='ACP',
    DATAFILETYPE = 'char',
    FIELDTERMINATOR= ',',
    KEEPIDENTITY,
    TABLOCK,
	FIRSTROW = 2,
	FORMAT = 'CSV'
);

-- ******************************************************

PRINT 'Loading Branch';
BULK INSERT Branch FROM '$(DataPath)Branch.csv'
WITH (
    CHECK_CONSTRAINTS,
    CODEPAGE='ACP',
    DATAFILETYPE = 'char',
    FIELDTERMINATOR= ',',
    KEEPIDENTITY,
    TABLOCK,
	FIRSTROW = 2,
	FORMAT = 'CSV'
);

-- ******************************************************

PRINT 'Loading Department';
INSERT INTO Department
SELECT b.BranchID, d.DeptName, 15000000 
FROM 
	Branch b, 
	(VALUES ('Kitchen'), ('Reception'), ('Waiter'), ('Cashier'), ('Manager')) AS d(DeptName)

-- ******************************************************

PRINT 'Loading BranchMenuItem';
INSERT INTO BranchMenuItem
SELECT b.BranchID, mi.ItemID, 1
FROM Branch b, MenuItem mi

-- ******************************************************

PRINT 'Loading CardType';
BULK INSERT CardType FROM '$(DataPath)CardType.csv'
WITH (
    CHECK_CONSTRAINTS,
    CODEPAGE='ACP',
    DATAFILETYPE = 'char',
    FIELDTERMINATOR= ',',
    KEEPIDENTITY,
    TABLOCK,
	FIRSTROW = 2,
	FORMAT = 'CSV'
);

-- ******************************************************

PRINT 'Loading Customer';
BULK INSERT Customer FROM '$(DataPath)Customer.csv'
WITH (
    CHECK_CONSTRAINTS,
    CODEPAGE='ACP',
    DATAFILETYPE = 'char',
    FIELDTERMINATOR= ',',
    KEEPIDENTITY,
    TABLOCK,
	FIRSTROW = 2,
	FORMAT = 'CSV'
);

-- *****************************************************

PRINT 'Loading Staff';
BULK INSERT Staff FROM '$(DataPath)Staff.csv'
WITH (
    CHECK_CONSTRAINTS,
    CODEPAGE='ACP',
    DATAFILETYPE = 'char',
    FIELDTERMINATOR= ',',
    KEEPIDENTITY,
    TABLOCK,
	FIRSTROW = 2,
	FORMAT = 'CSV'
);
-- Promote 1 at each branch to Manager
UPDATE s SET DeptName = 'Manager'
FROM STAFF s
WHERE s.StaffID = (SELECT MIN(StaffID) FROM STAFF WHERE BranchID = s.BranchID);

-- *****************************************************

PRINT 'Loading StaffInfo';
BULK INSERT StaffInfo FROM '$(DataPath)StaffInfo.csv'
WITH (
    CHECK_CONSTRAINTS,
    CODEPAGE='ACP',
    DATAFILETYPE = 'char',
    FIELDTERMINATOR= ',',
    KEEPIDENTITY,
    TABLOCK,
	FIRSTROW = 2,
	FORMAT = 'CSV'
);

---- ******************************************************

PRINT 'Loading Reservation';
BULK INSERT Reservation FROM '$(DataPath)Reservation.csv'
WITH (
    CHECK_CONSTRAINTS,
    CODEPAGE='ACP',
    DATAFILETYPE = 'char',
    FIELDTERMINATOR= ',',
    KEEPIDENTITY,
    TABLOCK,
	FIRSTROW = 2,
	FORMAT = 'CSV'
);

-- ******************************************************

PRINT 'Loading Coupon';
BULK INSERT Coupon FROM '$(DataPath)Coupon.csv'
WITH (
    CHECK_CONSTRAINTS,
    CODEPAGE='ACP',
    DATAFILETYPE = 'char',
    FIELDTERMINATOR= ',',
    KEEPIDENTITY,
    TABLOCK,
	FIRSTROW = 2,
	FORMAT = 'CSV'
);

-- ******************************************************
	
-- Let's pretend that every branch has the same amount of tables and seats
PRINT 'Loading Table';
INSERT INTO [Table] (BranchID, TableCode, NumOfSeats, isVacant)
SELECT 
	b.BranchID, 
	m.CategoryID, 
	IIF(m.CategoryID >= 20, 10, IIF(m.CategoryID >= 10, 5, 2)),
	1
FROM Branch b, MenuCategory m -- Menu Category is just for Catesian product

-- ******************************************************

PRINT 'Loading Order';
-- Historical Order
DECLARE @FromDate DATETIME = '2023-05-01';
DECLARE @ToDate DATETIME = GETDATE();
INSERT INTO [Order] (OrderDateTime, OrderStatus, StaffID, CustID, BranchID, OrderType)
SELECT TOP 100000
    DATEADD(hour, s.BranchID, DATEADD(day, RAND(CHECKSUM(NEWID())) * (1 + DATEDIFF(day, @FromDate, @ToDate)), @FromDate)),
    IIF(c.CustID % 23 = 0, 'CANCELLED', 'COMPLETED'),
    s.StaffID,
    c.CustID,
    s.BranchID,
    IIF(c.CustID % 7 = 0, 'D', 'I')
FROM Customer c, Staff s
WHERE s.BranchID = s.BranchID
ORDER BY NEWID()

-- Today Order
INSERT INTO [Order] (OrderDateTime, OrderStatus, StaffID, CustID, BranchID, OrderType)
SELECT TOP 2000
    DATEADD(hour, s.BranchID + 1, GETDATE()),
    CASE
		WHEN c.CustID % 4 = 0 THEN 'COMPLETED'
		WHEN c.CustID % 4 = 1 THEN 'DELIVERED'
		WHEN c.CustID % 4 = 2 THEN 'VERIFIED'
		ELSE 'UNVERIFIED'
	END,
    s.StaffID,
    c.CustID,
    s.BranchID,
    IIF(c.CustID % 7 = 0, 'D', 'I')
FROM Customer c, Staff s
WHERE s.BranchID = s.BranchID
ORDER BY NEWID()

-- ******************************************************

PRINT 'Loading OrderDetails'
INSERT INTO OrderDetails(OrderID, ItemID, UnitPrice, Quantity)
SELECT TOP 1000000 -- just to be save incase there is too many data
	o.OrderID,
	mi.ItemID,
	mi.UnitPrice,
	ABS(CHECKSUM(NEWID())) % 10 + 1
FROM [Order] o, MenuItem mi
WHERE
	((o.OrderID % 180 + 1) = mi.ItemID)
	OR (o.OrderID * ItemID % 41 = 0)

-- ******************************************************

PRINT 'Loading DineInOrder';
INSERT INTO DineInOrder (OrderID, TableCode, BranchID, RsID)
SELECT o.OrderID, o.orderID % 27 + 1, o.BranchID, NULL
FROM [Order] o
WHERE o.OrderType = 'I'

-- ******************************************************

PRINT 'Loading DeliveryOrder';
INSERT INTO DeliveryOrder (OrderID, DeliveryAddress, DeliveryDateTime)
SELECT o.OrderID, N'766 Võ Văn Kiệt, Phường 1, Quận 5, Hồ Chí Minh', DATEADD(hour, 1, o.OrderDateTime)
FROM [Order] o
WHERE o.OrderType = 'D'

-- ******************************************************

PRINT 'Loading Invoice'
INSERT INTO Invoice(OrderID, Subtotal, DiscountRate, TaxRate, ShippingCost, PaymentMethod, InvoiceDate, CouponID, BranchID, CustID)
SELECT 
	o.OrderID, 
	SUM(od.Quantity * od.UnitPrice),
	0,
	0.08,
	IIF(o.OrderType = 'D', 30000, 0),
	N'Tiền mặt',
	DATEADD(hour, 2, o.OrderDateTime),
	NULL,
	o.BranchID,
	o.CustID
FROM [Order] o
LEFT JOIN OrderDetails od
	ON o.OrderID = od.OrderID
WHERE o.OrderStatus = 'COMPLETED'
GROUP BY
	o.OrderID, o.OrderDateTime, o.BranchID, o.CustID, o.OrderType

-- ******************************************************

PRINT 'Loading MembershipCard'
INSERT INTO MembershipCard(IssuedDate, CardType, CustID, StaffID, Points)
SELECT '2024-03-01', 1, c.CustID, c.CustID % 280 + 1, SUM(Subtotal * (1 - i.DiscountRate) * (1 + i.taxRate))/100000
FROM Customer c
JOIN Invoice i ON c.CustID = i.CustID
WHERE c.CustID % 17 = 0 AND i.InvoiceDate >= '2024-03-01'
GROUP BY c.CustID

-- ******************************************************

PRINT 'Loading WorkHistory'
INSERT INTO WorkHistory(StaffID, StartDate, BranchID, DeptName, QuitDate)
SELECT s.StaffID, '2023-05-01', s.BranchID, s.DeptName, NULL
FROM Staff s

-- ******************************************************

PRINT 'Loading CustomerRating'
INSERT INTO CustomerRating(ServiceRating, LocationRating, FoodRating, PricingRating, AmbianceRating, FeedbackCmt, FeedbackDate, InvoiceID)
SELECT TOP 150
	ABS(CHECKSUM(NEWID())) % 10 + 1,
	ABS(CHECKSUM(NEWID())) % 10 + 1,
	ABS(CHECKSUM(NEWID())) % 10 + 1,
	ABS(CHECKSUM(NEWID())) % 10 + 1,
	ABS(CHECKSUM(NEWID())) % 10 + 1,
	N'Ngon hơn Tokyodeli, web xịn hơn Tokyodeli',
	DATEADD(minute, 15, i.InvoiceDate),
	i.InvoiceID
FROM Invoice i
ORDER BY NEWID();

-- ******************************************************

PRINT 'Loading StaffRating'
INSERT INTO StaffRating(RatingID, StaffID, StaffRating)
SELECT TOP 100
	cr.RatingID, ABS(CHECKSUM(NEWID())) % 180 + 1, ABS(CHECKSUM(NEWID())) % 10 + 1
FROM CustomerRating cr, Staff s
ORDER BY NEWID();


-- ******************************************************

-- Online access for customers who bought food online
PRINT 'Loading OnlineAccess'
INSERT INTO OnlineAccess(CustID, StartDateTime, EndDateTime)
SELECT DISTINCT o.CustID, DATEADD(minute, -10, o.OrderDateTime), DATEADD(minute, 5, o.OrderDateTime)
FROM [Order] o
WHERE o.OrderType = 'D'
ORDER BY o.CustID

-- ******************************************************

-- Check row count
CREATE TABLE #counts
(
    table_name varchar(255),
    row_count int
)
EXEC sp_MSForEachTable @command1='INSERT #counts (table_name, row_count) SELECT ''?'', COUNT(*) FROM ?'
SELECT table_name, row_count FROM #counts ORDER BY row_count DESC
DROP TABLE #counts