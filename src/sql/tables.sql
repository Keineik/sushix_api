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

USE $(DatabaseName);
GO

CREATE TABLE MenuCategory (
    CategoryID INT IDENTITY(1,1) PRIMARY KEY,
    CategoryName NVARCHAR(100) NOT NULL
);

CREATE TABLE MenuItem (
    ItemID INT IDENTITY(1,1) PRIMARY KEY,
    ItemName NVARCHAR(100) NOT NULL,
    UnitPrice DECIMAL(10,2) CHECK (UnitPrice >= 0),
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
    Salary DECIMAL(10,2),

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

    CONSTRAINT FK_Staff_Department FOREIGN KEY (BranchID, DeptName) REFERENCES Department(BranchID, DeptName)
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
    CustomerID INT DEFAULT NULL,
    StaffID INT DEFAULT NULL,
	IsAdmin BIT NOT NULL,

    CONSTRAINT FK_Account_Customer FOREIGN KEY (CustomerID) REFERENCES Customer(CustID),
    CONSTRAINT FK_Account_Staff FOREIGN KEY (StaffID) REFERENCES Staff(StaffID)
);

-- ******************************************************
-- Load data
-- ******************************************************
PRINT '';
PRINT '*** Loading Data';
GO

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

PRINT 'Loading BranchMenuItem';
INSERT INTO BranchMenuItem
SELECT b.BranchID, mi.ItemID, 1
FROM Branch b, MenuItem mi

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