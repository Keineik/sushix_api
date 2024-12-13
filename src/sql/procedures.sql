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

-- 2. Fetch Staffs
-- Fetch staffs with pagination. 
-- Search by StaffName and StaffID. 
-- Filter by BranchID, Deparment.
CREATE OR ALTER PROC usp_FetchStaffs
	@Page int = 1,
	@Limit int = 18,
	@SearchTerm nvarchar(100) = '', -- StaffID/StaffName
	@BranchID int = 0, --Filter
	@Department varchar(10) = ''
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Offset int = (@Page - 1) * @Limit;
	DECLARE @Search nvarchar(100) = '%' + @SearchTerm + '%';

	SELECT s.StaffID,
	s.StaffName,
	s.StaffDOB,
	s.StaffGender,
	s.DeptName,
	s.BranchID,
	s.isBranchManager
	FROM Staff s
	WHERE s.StaffID like @Search or s.StaffName like @Search
		AND (@BranchID = 0 OR s.BranchID = @BranchID)
		AND (@Department = '' OR s.DeptName = @Department)
	ORDER BY s.StaffID,
	s.StaffName,
	s.StaffDOB,
	s.StaffGender,
	s.DeptName,
	s.BranchID,
	s.isBranchManager
	OFFSET @Offset ROWS FETCH NEXT @Limit ROWS ONLY;
END
GO

EXEC sp_FetchStaffs

GO

-- 3. Fetch Reservation
-- Fetch reservations with pagination. 
-- JOIN with Customer table to get CustName, CustPhoneNumber, CustEmail. 
-- Search by ReservationID. 
-- Filter by BranchID. 
-- Sort by RsDatetime.
CREATE OR ALTER PROC usp_FetchReservation
	@Page INT = 1,
	@Limit INT = 18,
	@SearchTerm NVARCHAR(100) = '', -- ReservationID
	@BranchID INT = 0, -- Filter
	@SortDirection BIT = 0 -- 0: asc, 1: desc
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Offset INT = (@Page - 1) * @Limit;
	DECLARE @Search NVARCHAR(100) = '%' + @SearchTerm + '%';

	SELECT 
		r.RsID,
		r.CustID,
		r.NumOfGuests,
		r.RsDateTime,
		r.ArrivalDateTime,
		r.RsNotes,
		c.CustName,
		c.CustPhoneNumber,
		c.CustEmail
	FROM Reservation r
	JOIN Customer c ON r.CustID = c.CustID
	WHERE (r.RsID LIKE @Search)
		AND (@BranchID = 0 OR r.BranchID = @BranchID)
	ORDER BY 
    CASE 
		WHEN @SortDirection = 0 THEN r.RsDateTime
    END ASC,
    CASE 
		WHEN @SortDirection = 1 THEN r.RsDateTime
    END DESC
	OFFSET @Offset ROWS FETCH NEXT @Limit ROWS ONLY;
END
GO

EXEC usp_FetchReservation

Go
-- 
-- 4. Fetch Orders
-- Fetch orders with pagination. 
-- JOIN with Customer table to get CustName, CustPhoneNumber, CustEmail. 
-- Search by OrderID. 
-- Filter by BranchID, OrderType (Dine-In and Delivery) and OrderStatus (e.g., Preparing, Out for Delivery, Delivered, Completed, Cancelled). 
-- Sort by OrderDateTime, estimated total price (Someone got a better name?) . 
CREATE OR ALTER PROC usp_FetchOrders
	@Page INT = 1,
	@Limit INT = 18,
	@SearchTerm NVARCHAR(100) = '', -- Search by OrderID
	@BranchID INT = 0, -- Filter by Branch
	@OrderStatus NVARCHAR(50) = '', -- Filter by OrderStatus
	@OrderType NVARCHAR(10) = '', -- Filter by OrderType ('Dine-In', 'Delivery')
	@SortKey NVARCHAR(20) = 'OrderDateTime', -- Sort by OrderDateTime/EstimatedPrice
	@SortDirection BIT = 0 -- 0: asc, 1: desc
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Offset INT = (@Page - 1) * @Limit;
	DECLARE @Search NVARCHAR(100) = '%' + @SearchTerm + '%';

	SELECT 
		o.OrderID,
		o.OrderDateTime,
		o.OrderStatus,
		o.BranchID,
		c.CustName,
		c.CustPhoneNumber,
		c.CustEmail,
		CASE 
			WHEN do.OrderID IS NOT NULL THEN 'Delivery'
			WHEN dio.OrderID IS NOT NULL THEN 'Dine-In'
			ELSE 'Unknown'
		END AS OrderType,
		ISNULL(SUM(od.UnitPrice * od.OrderQuantity), 0) AS EstimatedPrice
	FROM [Order] o
	JOIN Customer c ON o.CustID = c.CustID
	LEFT JOIN DeliveryOrder do ON o.OrderID = do.OrderID
	LEFT JOIN DineInOrder dio ON o.OrderID = dio.OrderID
	LEFT JOIN OrderDetails od ON o.OrderID = od.OrderID
	WHERE (o.OrderID LIKE @Search)
		AND (@BranchID = 0 OR o.BranchID = @BranchID)
		AND (@OrderStatus = '' OR o.OrderStatus = @OrderStatus)
		AND (@OrderType = '' OR 
			(@OrderType = 'Delivery' AND do.OrderID IS NOT NULL) OR 
			(@OrderType = 'Dine-In' AND dio.OrderID IS NOT NULL))
	GROUP BY 
		o.OrderID,
		o.OrderDateTime,
		o.OrderStatus,
		o.BranchID,
		c.CustName,
		c.CustPhoneNumber,
		c.CustEmail,
		do.OrderID,
		dio.OrderID
	ORDER BY 
    CASE 
        WHEN @SortKey = 'OrderDateTime' AND @SortDirection = 0 THEN o.OrderDateTime
    END ASC,
    CASE 
        WHEN @SortKey = 'OrderDateTime' AND @SortDirection = 1 THEN o.OrderDateTime
    END DESC
	OFFSET @Offset ROWS FETCH NEXT @Limit ROWS ONLY;
END
GO

EXEC usp_FetchOrders

GO
-- 
-- 5. Fetch Invoices
-- - Fetch invoices with pagination. 
-- - JOIN with Customer table to get CustName, CustPhoneNumber, CustEmail. 
-- - Search by InvoiceID. 
-- - Filter by BranchID. 
-- - Sort by total price.

CREATE OR ALTER PROC usp_FetchInvoices
    @Page INT = 1,
    @Limit INT = 18,
    @SearchTerm NVARCHAR(100) = '', -- Search by InvoiceID
    @BranchID INT = 0, -- Filter by BranchID
    @SortDirection BIT = 0 -- 0: ASC, 1: DESC
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Offset INT = (@Page - 1) * @Limit;
    DECLARE @Search NVARCHAR(100) = '%' + @SearchTerm + '%';

    SELECT 
        i.InvoiceID,
        i.OrderID,
        i.InvoiceDate,
        i.PaymentMethod,
        i.ShippingCost,
        ISNULL(SUM(od.UnitPrice * od.OrderQuantity), 0) AS TotalPrice,
        c.CustName,
        c.CustPhoneNumber,
        c.CustEmail
    FROM Invoice i
    JOIN [Order] o ON i.OrderID = o.OrderID
    JOIN Customer c ON o.CustID = c.CustID
    LEFT JOIN OrderDetails od ON o.OrderID = od.OrderID
    WHERE (CAST(i.InvoiceID AS NVARCHAR) LIKE @Search)
        AND (@BranchID = 0 OR o.BranchID = @BranchID)
    GROUP BY 
        i.InvoiceID,
        i.OrderID,
        i.InvoiceDate,
        i.PaymentMethod,
        i.ShippingCost,
        c.CustName,
        c.CustPhoneNumber,
        c.CustEmail
    ORDER BY 
        CASE 
            WHEN @SortDirection = 0 THEN SUM(od.UnitPrice * od.OrderQuantity)
        END ASC,
        CASE 
            WHEN @SortDirection = 1 THEN SUM(od.UnitPrice * od.OrderQuantity)
        END DESC
    OFFSET @Offset ROWS FETCH NEXT @Limit ROWS ONLY;
END;

GO
-- 
-- 6. Fetch Customers
-- - Fetch customers with pagination. 
-- - Search by Name or CustomerID (?). 
CREATE OR ALTER PROC usp_FetchCustomers
    @Page INT = 1,
    @Limit INT = 18,
    @SearchTerm NVARCHAR(100) = '' -- Search by CustID or CustName
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Offset INT = (@Page - 1) * @Limit;
    DECLARE @Search NVARCHAR(100) = '%' + @SearchTerm + '%';

    SELECT 
        CustID,
        CustName,
        CustEmail,
        CustGender,
        CustPhoneNumber,
        CustCitizenID
    FROM Customer
    WHERE (CAST(CustID AS NVARCHAR) LIKE @Search OR CustName LIKE @Search)
    ORDER BY CustID
    OFFSET @Offset ROWS FETCH NEXT @Limit ROWS ONLY;
END;
GO

-- 
-- 7. Fetch Coupons
-- - Fetch coupons with pagination. 
-- - Search by ID, code. 
-- - Filter by MinOrderValue, type of MinMembershipRequirement, Status(available/unavailable). 
-- - Sort?
CREATE OR ALTER PROC usp_FetchCoupons
    @Page INT = 1,
    @Limit INT = 18,
    @SearchTerm NVARCHAR(100) = '', -- Search by CouponID or CouponCode
    @MinPurchase DECIMAL(19,4) = 0, -- Filter by MinPurchase
    @MinMembershipRequirement INT = NULL, -- Filter by MembershipRequirement
    @Status NVARCHAR(10) = '', -- Filter by Status ('available' or 'unavailable')
    @SortDirection BIT = 0 -- 0: ASC, 1: DESC
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Offset INT = (@Page - 1) * @Limit;
    DECLARE @Search NVARCHAR(100) = '%' + @SearchTerm + '%';

    SELECT 
        CouponID,
        CouponCode,
        CouponDesc,
        DiscountRate,
        MinPurchase,
        MaxDiscount,
        EffectiveDate,
        ExpiryDate,
        TotalUsageLimit,
        MinMembershipRequirement,
        CASE 
            WHEN GETDATE() BETWEEN EffectiveDate AND ExpiryDate THEN 'Available'
            ELSE 'Unavailable'
        END AS Status
    FROM Coupon
    WHERE (CAST(CouponID AS NVARCHAR) LIKE @Search OR CouponCode LIKE @Search)
        AND (MinPurchase >= @MinPurchase)
        AND (@MinMembershipRequirement IS NULL OR MinMembershipRequirement = @MinMembershipRequirement)
        AND (@Status = '' OR 
             (@Status = 'available' AND GETDATE() BETWEEN EffectiveDate AND ExpiryDate) OR 
             (@Status = 'unavailable' AND GETDATE() NOT BETWEEN EffectiveDate AND ExpiryDate))
    ORDER BY 
        CASE 
            WHEN @SortDirection = 0 THEN MinPurchase
        END ASC,
        CASE 
            WHEN @SortDirection = 1 THEN MinPurchase
        END DESC
    OFFSET @Offset ROWS FETCH NEXT @Limit ROWS ONLY;
END;
GO

-- 8. Fetch statistics for a branch
-- - (e.g., number of orders, revenue, online accesss) I have no idea 

