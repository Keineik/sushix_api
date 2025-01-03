/*
 * HOW TO RUN THIS SCRIPT:
 * Run table.sql first then run this one
 */

USE SushiX
GO

DECLARE @NewLine AS CHAR(2) = CHAR(13) + CHAR(10)

DECLARE @DBName VARCHAR(10) = 'SushiX';
DECLARE @PartitionFilePath VARCHAR(255) = 'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\';

DECLARE @PartitionDateValues NVARCHAR(MAX) = '(';
DECLARE @PartitionFGValues NVARCHAR(MAX) = '(';

-- Create file groups and files in it up until the current month
DECLARE @PartitionDate DATE = '2023-05-01';
WHILE (@PartitionDate < GETDATE())
BEGIN
	DECLARE @FileGroup NVARCHAR(127) = CONCAT('FG_', YEAR(@PartitionDate), '_', MONTH(@PartitionDate));
	DECLARE @AddFileGroupScript NVARCHAR(MAX) = CONCAT(
		'ALTER DATABASE ', @DBName, @NewLine,
		'ADD FILEGROUP ', @FileGroup, @NewLine
	);

	DECLARE @FileName NVARCHAR(127) = CONCAT('File_', YEAR(@PartitionDate), '_', MONTH(@PartitionDate));
	DECLARE @AddFileScript NVARCHAR(MAX) = CONCAT (
		'ALTER DATABASE ', @DBName, @NewLine,
		'ADD FILE (', @NewLine,
			'NAME = ''', @FileName, ''',', @NewLine,
			'FILENAME = ''', @PartitionFilePath, @FileName, '.ndf''', ',', @NewLine,
			'SIZE = 5 MB', ',', @NewLine,
			'MAXSIZE = UNLIMITED', ',', @NewLine,
			'FILEGROWTH = 1 MB', @NewLine,
		')', 'TO FILEGROUP ', @FileGroup, @NewLine
	);

	-- Create file group and add file to file group
	EXEC (@AddFileGroupScript);
	EXEC (@AddFileScript);

	-- Update values for partition function and scheme
	DECLARE @EndingString VARCHAR(5) = IIF(DATEADD(MONTH, 1, @PartitionDate) < GETDATE(), ', ', '');
	SET @PartitionDateValues = @PartitionDateValues + CONCAT('''', @PartitionDate, '''', @EndingString);
	SET @PartitionFGValues = @PartitionFGValues + CONCAT(@FileGroup, ', ');

	SET @PartitionDate = DATEADD(MONTH, 1, @PartitionDate);
END
SET @PartitionDateValues = @PartitionDateValues + ')';
SET @PartitionFGValues = @PartitionFGValues + '[PRIMARY])';

-- Confirm Filegroups
SELECT name as [File Group Name]
FROM sys.filegroups
WHERE type = 'FG'

-- Confirm Datafiles
SELECT name as [DB FileName],physical_name as
[DB File Path]
FROM sys.database_files
where type_desc = 'ROWS'

-- Create partition function
DECLARE @PartitionFunctionScript VARCHAR(MAX) = CONCAT(
	'CREATE PARTITION FUNCTION MONTHLY_PF(DATETIME)', @NewLine,
	'AS RANGE RIGHT', @NewLine,
	'FOR VALUES', @PartitionDateValues
)
EXEC (@PartitionFunctionScript)

-- Create partition scheme
DECLARE @PartitionSchemeScript VARCHAR(MAX) = CONCAT(
	'CREATE PARTITION SCHEME MONTHLY_PS', @NewLine,
	'AS PARTITION MONTHLY_PF', @NewLine,
	'TO', @PartitionFGValues
)
EXEC (@PartitionSchemeScript)

-- Create partition for Invoice
CREATE CLUSTERED INDEX CI_Invoice_InvoiceDate
ON Invoice(InvoiceDate) ON MONTHLY_PS(InvoiceDate);
-- Create partition for Order
CREATE CLUSTERED INDEX CI_Order_OrderDateTime
ON [Order](OrderDateTime) ON MONTHLY_PS(OrderDateTime);

-- Check partition results
SELECT 
     TableName = t.name,
     IndexName = ind.name,
     IndexId = ind.index_id,
     ColumnId = ic.index_column_id,
     ColumnName = col.name,
	 ind.[type_desc],
	 [is_unique],
	 [is_primary_key]
     --ind.*,
     --ic.*,
     --col.* 
FROM sys.indexes ind 
JOIN sys.index_columns ic ON  ind.object_id = ic.object_id and ind.index_id = ic.index_id 
JOIN sys.columns col ON ic.object_id = col.object_id and ic.column_id = col.column_id 
JOIN sys.tables t ON ind.object_id = t.object_id 
WHERE t.name IN ('Order', 'Invoice')
ORDER BY t.name, ind.name, ind.index_id, ic.is_included_column, ic.key_ordinal;

-- Check number of rows in each partition
SELECT DISTINCT o.name as table_name, rv.value as partition_range, fg.name as file_groupName, p.partition_number, p.rows as number_of_rows
FROM sys.partitions p
JOIN sys.indexes i ON p.object_id = i.object_id AND p.index_id = i.index_id
JOIN sys.objects o ON p.object_id = o.object_id
JOIN sys.system_internals_allocation_units au ON p.partition_id = au.container_id
JOIN sys.partition_schemes ps ON ps.data_space_id = i.data_space_id
JOIN sys.partition_functions f ON f.function_id = ps.function_id
JOIN sys.destination_data_spaces dds ON dds.partition_scheme_id = ps.data_space_id AND dds.destination_id = p.partition_number
JOIN sys.filegroups fg ON dds.data_space_id = fg.data_space_id 
LEFT JOIN sys.partition_range_values rv ON f.function_id = rv.function_id AND p.partition_number = rv.boundary_id
WHERE o.object_id = OBJECT_ID('Order')
	OR o.object_id = OBJECT_ID('Invoice');