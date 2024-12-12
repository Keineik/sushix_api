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
:setvar DataPath "D:\Projects\sushix_api\src\sql\data\"

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
    DROP DATABASE $(DatabaseName);

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

drop database SushiX

USE $(DatabaseName);
GO

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
    IsDiscontinued BIT,
	ImgUrl NVARCHAR(2083),

    CONSTRAINT FK_MenuItem_MenuCategory FOREIGN KEY (CategoryID) REFERENCES MenuCategory(CategoryID)
);

CREATE TABLE MenuCombo (
    ComboID INT IDENTITY(1,1) PRIMARY KEY,
    ComponentID INT,

	CONSTRAINT FK_MenuCombo_MenuItem1 FOREIGN KEY (ComboID) REFERENCES MenuItem(ItemID),
    CONSTRAINT FK_MenuCombo_MenuItem2 FOREIGN KEY (ComponentID) REFERENCES MenuItem(ItemID)
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
    StaffName VARCHAR(100) NOT NULL,
    StaffDOB DATE,
    StaffGender CHAR(1) CHECK (StaffGender IN ('M', 'F')),
    DeptName VARCHAR(10),
    BranchID INT,
	isBranchManager BIT DEFAULT 0,

    CONSTRAINT FK_Staff_Department FOREIGN KEY (BranchID, DeptName) REFERENCES Department(BranchID, DeptName)
);

CREATE TABLE WorkHistory (
	StaffID INT,
	StartDate DATE,
	DeptID INT NOT NULL,
	QuitDate DATE,

	CONSTRAINT PK_WorkHistory PRIMARY KEY (StaffID, StartDate),
	CONSTRAINT FK_WorkHistory_Department FOREIGN KEY (DeptID) REFERENCES Department(DeptID)
);

CREATE TABLE Customer (
    CustID INT IDENTITY(1,1) PRIMARY KEY,
    CustName VARCHAR(255) NOT NULL,
    CustEmail VARCHAR(255),
    CustGender CHAR(1) CHECK (CustGender IN ('M', 'F')),
    CustPhoneNumber VARCHAR(20),
    CustCitizenID VARCHAR(20) UNIQUE
);

CREATE TABLE CardType (
    CardTypeID INT IDENTITY(1,1) PRIMARY KEY,
    CardName VARCHAR(10) NOT NULL,
    DiscountRate DECIMAL(5,2) CHECK (DiscountRate BETWEEN 0 AND 100),
    PointsRequiredForRenewal INT CHECK (PointsRequiredForRenewal >= 0),
    PointsRequiredForUpgrade INT CHECK (PointsRequiredForUpgrade >= 0)
);
	
CREATE TABLE MembershipCard (
    CardID INT IDENTITY(1,1) PRIMARY KEY,
    IssuedDate DATE DEFAULT GETDATE(),
    CardType INT NOT NULL,
    CustID INT NOT NULL,

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
	CustID INT,
	StartDateTime DATETIME,
	EndDateTime DATETIME,

	CONSTRAINT PK_OnlineAccess PRIMARY KEY (CustID, StartDateTime),
	CONSTRAINT FK_OnlineAccess FOREIGN KEY (CustID) REFERENCES Customer(CustID)
);

CREATE TABLE Reservation (
	RsID INT IDENTITY(1, 1) PRIMARY KEY,
	NumOfGuests INT,
	RsDateTime DateTime,
	ArrivalDateTime DateTime,
	RsNotes NVARCHAR(1000),
	BranchID INT NOT NULL,
	CustID INT NOT NULL,

	CONSTRAINT FK_Reservation_Branch FOREIGN KEY (BranchID) REFERENCES Branch(BranchID),
	CONSTRAINT FK_Reservation_Customter FOREIGN KEY (CustID) REFERENCES Customer(CustID)
);

CREATE TABLE [Order] (
	OrderID INT IDENTITY(1, 1) PRIMARY KEY,
	OrderDateTime DateTime,
	OrderStatus NVARCHAR(50),
	StaffID INT,
	CustID INT NOT NULL,
	BranchID INT NOT NULL,

	CONSTRAINT FK_Order_Customer FOREIGN KEY (CustID) REFERENCES Customer(CustID),
	CONSTRAINT FK_Order_Branch FOREIGN KEY (BranchID) REFERENCES Branch(BranchID)
);

CREATE TABLE OrderDetails (
	OrderID INT,
	ItemID INT,
	UnitPrice DECIMAL(19,4) CHECK (UnitPrice >= 0),
	OrderQuantity INT CHECK (OrderQuantity > 0),

	CONSTRAINT PK_OrderDetails PRIMARY KEY (OrderID, ItemID),
	CONSTRAINT FK_OrderDetails_Order FOREIGN KEY (OrderID) REFERENCES [Order](OrderID),
	CONSTRAINT FK_OrderDetails_Item FOREIGN KEY (ItemID) REFERENCES MenuItem(ItemID),
);

CREATE TABLE [Table] (
	TableID INT,
	BranchID INT,
	NumOfSeats INT,
	isVacant BIT NOT NULL,

	CONSTRAINT PK_Table PRIMARY KEY (TableID, BranchID),
	CONSTRAINT FK_Table_Branch FOREIGN KEY (BranchID) REFERENCES Branch(BranchID),
);

CREATE TABLE DineInOrder (
	OrderID INT PRIMARY KEY,
	TableID INT,
	BranchID INT,
	RsID INT,

	CONSTRAINT FK_DineInOrder_Order FOREIGN KEY (OrderID) REFERENCES [Order](OrderID),
	CONSTRAINT FK_DineInOrder_Table FOREIGN KEY (TableID, BranchID) REFERENCES [Table](TableID, BranchID),
	CONSTRAINT FK_DineInOrder_Reservation FOREIGN KEY (RsID) REFERENCES [Order](OrderID)
);

CREATE TABLE DeliveryOrder (
	OrderID INT PRIMARY KEY,
	DeliveryAddress NVARCHAR(1000) NOT NULL,
	DeliveryDateTime DATETIME,

	CONSTRAINT FK_DeliveryOrder_Order FOREIGN KEY (OrderID) REFERENCES [Order](OrderID),
);

CREATE TABLE Invoice(
	InvoiceID INT IDENTITY(1, 1) PRIMARY KEY,
	OrderID INT NOT NULL,
);

CREATE TABLE CustomerRating (
	RatingID INT IDENTITY(1, 1) PRIMARY KEY,
	ServiceRating INT CHECK(ServiceRating >= 1 and ServiceRating <= 10),
	LocationRating INT CHECK(LocationRating >= 1 and LocationRating <= 10),
	FoodRating INT CHECK(FoodRating >= 1 and FoodRating <= 10),
	PricingRating INT CHECK(PricingRating >= 1 and PricingRating <= 10),
	AmbianceRating INT CHECK(AmbianceRating >= 1 and AmbianceRating <= 10),
	FeedbackCmt NVARCHAR(MAX),
	FeedbackDate DATE,
	InvoiceID INT NOT NULL,
	BranchID INT,

	CONSTRAINT FK_CustomerRating_Invoice FOREIGN KEY (InvoiceID) REFERENCES Invoice(InvoiceID),
	CONSTRAINT FK_CustomerRating_Branch FOREIGN KEY (BranchID) REFERENCES Branch(BranchID)
);

CREATE TABLE StaffRating (
	RatingID INT,
	StaffID INT,
	StaffRating INT NOT NULL,

	CONSTRAINT PK_StaffRating PRIMARY KEY (RatingID, StaffID),
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