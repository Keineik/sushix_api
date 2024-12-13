Use SushiX
GO

--1. Fetch Items
---Fetch items with pagination.
---Search by name, ItemID. (?)
---Filter by BranchID, category.
---Sort by price, ID.



create or alter proc usp_FetchItems
    @Page int = 1,
    @Limit int = 18,
    @SearchTerm nvarchar(100) = '', -- ItemID or ItemName
    @CategoryID int = 0, -- Filter
    @BranchID int = 0, -- Filter
    @SortKey varchar(10) = 'ID', -- Price or ID
    @SortDirection bit = 0 -- 0: asc, 1: desc
as
begin
	SET NOCOUNT ON;
	
    declare @Offset int = (@Page - 1) * @Limit;
    declare @Search nvarchar(100) = '%' + @SearchTerm + '%';

	select mi.ItemID,
		mi.ItemName,
		mi.UnitPrice,
		mi.ServingUnit,
		mi.CategoryID,
		mi.IsDiscontinued,
		mi.ImgUrl
	from MenuItem mi left join BranchMenuItem bmi on (bmi.ItemID = mi.ItemID and @BranchID != 0)
	where (mi.ItemName like @Search or mi.ItemID like @Search)
		and (@CategoryID = 0 or mi.CategoryID = @CategoryID)
		and (@BranchID = 0 or bmi.BranchID = @BranchID)
	group by mi.ItemID, mi.ItemName, mi.UnitPrice, mi.ServingUnit, mi.CategoryID, mi.IsDiscontinued, mi.ImgUrl
	order by 
    case 
        when @SortKey = 'price' AND @SortDirection = 0 then UnitPrice
        when @SortKey = 'ID' AND @SortDirection = 0 then mi.ItemID
    end asc,
    case 
        when @SortKey = 'price' AND @SortDirection = 1 then UnitPrice
        when @SortKey = 'ID' AND @SortDirection = 1 then mi.ItemID
    end desc
    offset @Offset rows fetch next @Limit rows only;
    
end;
GO
exec usp_FetchItems
GO