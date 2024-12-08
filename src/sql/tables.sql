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

CREATE TABLE BranchMenuItem (	
    BranchID INT,
    ItemID INT,
    IsShippable BIT,
    CONSTRAINT PK_BranchMenuItem PRIMARY KEY (BranchID, ItemID),
    CONSTRAINT FK_BranchMenuItem_Branch FOREIGN KEY (BranchID) REFERENCES Branch(BranchID),
    CONSTRAINT FK_BranchMenuItem_MenuItem FOREIGN KEY (ItemID) REFERENCES MenuItem(ItemID)
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