Use SushiX
GO

--1. Fetch Items
---Fetch items with pagination.
---Search by name, ItemID. (?)
---Filter by BranchID, category.
---Sort by price, ID.
create proc sp_FetchItems
    @Page int = 1,
    @Limit int = 18,
    @SearchTerm nvarchar(100) = '', --ItemID or ItemName
    @CategoryID int = 0, --Filter
    @BranchID int = 0,--Filter
    @SortKey varchar(10) = 'ID', --Price or ID
    @SortDirection bit = 0 --0: asc, 1: desc
as
begin

    declare @Offset int = (@Page - 1) * @Limit;
    declare @Search nvarchar(100) = '%' + @SearchTerm + '%';

    select * 
    from BranchMenuItem bmi join MenuItem mi on bmi.ItemID = mi.ItemID
    where mi.ItemName LIKE @Search
      and (@CategoryID = 0 or mi.CategoryID = @CategoryID)
      and (@BranchID = 0 or bmi.BranchID = @BranchID)
    order by 
      case when @SortKey = 'price' AND @SortDirection = 0 then mi.UnitPrice end asc,
	  case when @SortKey = 'price' and @SortDirection = 1 then mi.UnitPrice end desc,
      case when @SortKey = 'ID' and @SortDirection = 0 then mi.ItemID end asc,
	  case when @SortKey = 'ID' and @SortDirection = 1 then mi.ItemID end desc
    offset @Offset rows fetch next @Limit rows only;
END;
GO
exec sp_FetchItems
GO
