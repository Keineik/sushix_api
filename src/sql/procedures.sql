Use SushiX
GO

--1. Fetch Items
---Fetch items with pagination.
---Search by ItemName
---Filter by BranchID, category.
---Sort by price, ID.
create or alter proc usp_FetchItems
    @Page int = 1,
    @Limit int = 18,
    @SearchTerm nvarchar(100) = '', -- ItemName
    @CategoryID int = 0, -- Filter
    @BranchID int = 0, -- Filter
	@FilterShippable bit = 0, --0: no filter, 1: yes XD
    @SortKey varchar(10) = 'ID', -- 'price' or 'ID'
    @SortDirection bit = 0 -- 0: asc, 1: desc
as
begin
	SET NOCOUNT ON;
	
    declare @Offset int = (@Page - 1) * @Limit;
    declare @Search nvarchar(100) = @SearchTerm + '%';

	select mi.ItemID,
		mi.ItemName,
		mi.UnitPrice,
		mi.ServingUnit,
		mi.CategoryID,
		mi.SoldQuantity,
		mi.IsDiscontinued,
		mi.ImgUrl
	from MenuItem mi left join BranchMenuItem bmi on (bmi.ItemID = mi.ItemID)
	where (mi.ItemName like @Search)
		and (@CategoryID = 0 or mi.CategoryID = @CategoryID)
		and (@BranchID = 0 or bmi.BranchID = @BranchID)
		and (@FilterShippable = 0 or bmi.IsShippable = 1)
	group by mi.ItemID, mi.ItemName, mi.UnitPrice, mi.ServingUnit, mi.CategoryID, mi.SoldQuantity, mi.IsDiscontinued, mi.ImgUrl
	order by 
    case when @SortKey = 'price' AND @SortDirection = 0 then UnitPrice end,
    case when @SortKey = 'ID' AND @SortDirection = 0 then mi.ItemID end,
    case when @SortKey = 'price' AND @SortDirection = 1 then UnitPrice end desc,
    case when @SortKey = 'ID' AND @SortDirection = 1 then mi.ItemID end desc
    offset @Offset rows fetch next @Limit rows only
end;

GO
create or alter proc usp_FetchItems_count
    @SearchTerm nvarchar(100) = '', -- ItemID or ItemName
    @CategoryID int = 0, -- Filter
    @BranchID int = 0, -- Filter
	@FilterShippable bit = 0, --0: no filter, 1: yes XD
	@count int out
as
begin
	SET NOCOUNT ON;

    declare @Search nvarchar(100) = @SearchTerm + '%';

	select @count = count(distinct(mi.ItemID))
	from MenuItem mi left join BranchMenuItem bmi on (bmi.ItemID = mi.ItemID and @BranchID != 0)
	where (mi.ItemName like @Search or mi.ItemID like @Search)
		and (@CategoryID = 0 or mi.CategoryID = @CategoryID)
		and (@BranchID = 0 or bmi.BranchID = @BranchID)
		and (@FilterShippable = 0 or bmi.IsShippable = 1)
end;
GO

-- 2. Fetch Staffs
--Fetch staffs with pagination. 
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
    DECLARE @Search nvarchar(100) = @SearchTerm + '%';

    SELECT s.StaffID,
    s.DeptName,
    s.BranchID,
    si.StaffName,
    si.StaffDOB,
    si.StaffGender,
    si.StaffPhoneNumber,
    si.StaffCitizenID
    FROM Staff s join StaffInfo si ON s.StaffID = si.StaffID
    WHERE s.StaffID like @Search or si.StaffName like @Search
        AND (@BranchID = 0 OR s.BranchID = @BranchID)
        AND (@Department = '' OR s.DeptName = @Department)
    ORDER BY s.StaffID,
    s.DeptName,
    s.BranchID,
    si.StaffName,
    si.StaffDOB,
    si.StaffGender,
    si.StaffPhoneNumber,
    si.StaffCitizenID
    OFFSET @Offset ROWS FETCH NEXT @Limit ROWS ONLY;
END
GO

EXEC usp_FetchStaffs

GO

CREATE OR ALTER PROC usp_FetchStaffs_count 
	@SearchTerm nvarchar(100) = '', -- StaffID/StaffName
    @BranchID int = 0, --Filter
    @Department varchar(10) = '',
	@Count int out
AS
BEGIN
	SET NOCOUNT ON;
	DECLARE @Search nvarchar(100) = @SearchTerm + '%';
	SELECt  @count = count(distinct(s.StaffID))
	FROM Staff s join StaffInfo si ON s.StaffID = si.StaffID
    WHERE s.StaffID like @Search or si.StaffName like @Search
        AND (@BranchID = 0 OR s.BranchID = @BranchID)
        AND (@Department = '' OR s.DeptName = @Department)
END

GO

-- 3. Fetch Reservation
-- Fetch reservations with pagination. 
-- JOIN with Customer table to get CustName, CustPhoneNumber, CustEmail. 
-- Search by ReservationID. 
-- Filter by BranchID. 
-- Sort by RsDatetime.
CREATE OR ALTER PROC usp_FetchReservations
	@Page INT = 1,
	@Limit INT = 18,
	@SearchTerm NVARCHAR(100) = '', -- ReservationID
	@Status NVARCHAR(30) = '', --'' for all. 'Confirmed' or 'Not Confirmed' or 'Cancelled'
	@BranchID INT = 0, -- Filter
	@SortDirection BIT = 0 -- 0: asc, 1: desc
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Offset INT = (@Page - 1) * @Limit;
	DECLARE @Search NVARCHAR(100) = @SearchTerm + '%';

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
	WHERE (r.RsID LIKE @Search OR  (c.CustName LIKE @Search) 
         OR (CAST(c.CustPhoneNumber AS NVARCHAR) LIKE @Search))
		AND (@BranchID = 0 OR r.BranchID = @BranchID)
		AND (@Status = '' OR r.RsStatus = @Status)
	ORDER BY 
    CASE WHEN @SortDirection = 0 THEN r.RsDateTime END,
    CASE WHEN @SortDirection = 1 THEN r.RsDateTime END DESC
	OFFSET @Offset ROWS FETCH NEXT @Limit ROWS ONLY;
END

GO

CREATE OR ALTER PROC usp_FetchReservations_count
	@SearchTerm NVARCHAR(100) = '', -- ReservationID
	@Status NVARCHAR(30) = '', --'' for all. 'Confirmed' or 'Not Confirmed' or 'Cancelled'
	@BranchID INT = 0, -- Filter
	@Count INT OUT
AS
BEGIN
	SET NOCOUNT ON;
	DECLARE @Search NVARCHAR(100) = @SearchTerm + '%';

	SELECT @Count= COUNT(DISTINCT(r.RsID))
	FROM Reservation r
	JOIN Customer c ON r.CustID = c.CustID
	WHERE (r.RsID LIKE @Search OR  (c.CustName LIKE @Search) 
         OR (CAST(c.CustPhoneNumber AS NVARCHAR) LIKE @Search))
		AND (@BranchID = 0 OR r.BranchID = @BranchID)
		AND (@Status = '' OR r.RsStatus = @Status)
END
GO

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
	@CustID INT = 0,
	@OrderStatus NVARCHAR(10) = '', -- Filter by OrderStatus
	@OrderType NVARCHAR(10) = '', -- Filter by OrderType ('Dine-In', 'Delivery')
	@SortKey NVARCHAR(20) = 'OrderDateTime', -- Sort by OrderDateTime/EstimatedPrice
	@SortDirection BIT = 0 -- 0: asc, 1: desc
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Offset INT = (@Page - 1) * @Limit;
	DECLARE @Search NVARCHAR(100) = @SearchTerm + '%';

	SELECT 
		o.OrderID,
		o.OrderDateTime,
		o.OrderStatus,
		o.BranchID,
		c.CustID,
		c.CustName,
		c.CustPhoneNumber,
		c.CustEmail,
		IIF(o.orderType = 'D', 'Delivery', 'Dine-In'),
		ISNULL(SUM(od.UnitPrice * od.Quantity), 0) AS EstimatedPrice
	FROM [Order] o
	JOIN Customer c ON o.CustID = c.CustID
	LEFT JOIN OrderDetails od ON o.OrderID = od.OrderID
	WHERE ((o.OrderID LIKE @Search) 
		OR (c.CustName LIKE @Search) 
        OR (CAST(c.CustPhoneNumber AS NVARCHAR) LIKE @Search))
		AND (@BranchID = 0 OR o.BranchID = @BranchID)
		AND (@CustID = 0 OR c.CustID = @CustID)
		AND (@OrderStatus = '' OR o.OrderStatus = @OrderStatus)
		AND (@OrderType = '' OR @OrderType = o.OrderType)
	GROUP BY 
		o.OrderID,
		o.OrderDateTime,
		o.OrderStatus,
		o.BranchID,
		o.OrderType,
		c.CustID,
		c.CustName,
		c.CustPhoneNumber,
		c.CustEmail
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
exec usp_FetchOrders
GO
CREATE OR ALTER PROC usp_FetchOrders_count
	@SearchTerm NVARCHAR(100) = '', -- Search by OrderID
	@BranchID INT = 0, -- Filter by Branch
	@CustID INT = 0,
	@OrderStatus NVARCHAR(50) = '', -- Filter by OrderStatus
	@OrderType CHAR(1) = '', -- Filter by OrderType ('D' for DeliveryOrder, 'I' for DineInOrder)
	@Count INT OUT
AS
BEGIN
	SET NOCOUNT ON;
	DECLARE @Search NVARCHAR(100) = @SearchTerm + '%';
	SELECT @Count = COUNT(DISTINCT o.OrderID)
	FROM [Order] o
	JOIN Customer c ON o.CustID = c.CustID
	LEFT JOIN OrderDetails od ON o.OrderID = od.OrderID
	WHERE ((o.OrderID LIKE @Search) 
		OR (c.CustName LIKE @Search) 
        OR (CAST(c.CustPhoneNumber AS NVARCHAR) LIKE @Search))
		AND (@BranchID = 0 OR o.BranchID = @BranchID)
		AND (@CustID = 0 OR c.CustID = @CustID)
		AND (@OrderStatus = '' OR o.OrderStatus = @OrderStatus)
		AND (@OrderType = '' OR @OrderType = o.OrderType)
END


GO

-- 5. Fetch Invoices
-- - Fetch invoices with pagination. 
-- - JOIN with Customer table to get CustName, CustPhoneNumber, CustEmail. 
-- - Search by InvoiceID. 
-- - Filter by BranchID. 
-- - Sort by total price.

CREATE OR ALTER PROC usp_FetchInvoices
    @Page INT = 1, 
    @Limit INT = 18, 
    @SearchTerm NVARCHAR(100) = '', -- Search by InvoiceID, Customer Name
    @BranchID INT = 0, -- Filter by BranchID
    @StartDate DATE = '', -- Filter by Start Date
    @EndDate DATE = '', -- Filter by End Date
    @SortDirection BIT = 0 -- 0: ASC, 1: DESC
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Offset INT = (@Page - 1) * @Limit;
    DECLARE @Search NVARCHAR(100) = '%' + @SearchTerm + '%';

    SELECT 
        i.InvoiceID,
        i.OrderID,
		o.BranchID,
        i.InvoiceDate,
        i.PaymentMethod,
        i.ShippingCost,
        i.Subtotal,
        i.TaxRate,
        i.DiscountRate,
        i.CouponID,
        (i.Subtotal * (1 - ISNULL(ct.DiscountRate, 0)) - 
            CASE 
                WHEN i.CouponID IS NULL THEN 0
                WHEN cp.DiscountFlat IS NOT NULL THEN ISNULL(cp.DiscountFlat, 0)
                WHEN cp.DiscountRate IS NOT NULL THEN 
                    CASE 
                        WHEN (i.DiscountRate * i.Subtotal) > ISNULL(cp.MaxDiscount, 0) 
                        THEN ISNULL(cp.MaxDiscount, 0)
                        ELSE i.DiscountRate * i.Subtotal
                    END
                ELSE 0 
            END
        + ISNULL(i.ShippingCost, 0)) * (1 + ISNULL(i.TaxRate, 0)) AS Total,
		c.CustID,
        c.CustName,
        c.CustPhoneNumber,
        c.CustEmail
    FROM Invoice i
    JOIN [Order] o ON i.OrderID = o.OrderID
    JOIN Customer c ON o.CustID = c.CustID
    LEFT JOIN Coupon cp ON cp.CouponID = i.CouponID
    LEFT JOIN MembershipCard m ON m.CustID = c.CustID
    LEFT JOIN CardType ct ON ct.CardTypeID = m.CardType
    WHERE 
        (CAST(i.InvoiceID AS NVARCHAR) LIKE @Search 
         OR c.CustName LIKE @Search 
         OR CAST(c.CustPhoneNumber AS NVARCHAR) LIKE @Search)
        AND (@BranchID = 0 OR o.BranchID = @BranchID)
        AND (@StartDate = '' OR (CAST(i.InvoiceDate AS DATE) >= @StartDate))
        AND (@EndDate = '' OR (CAST(i.InvoiceDate AS DATE) <= @EndDate))
    ORDER BY 
        CASE WHEN @SortDirection = 0 THEN i.InvoiceDate END ASC,
        CASE WHEN @SortDirection = 1 THEN i.InvoiceDate END DESC
    OFFSET @Offset ROWS FETCH NEXT @Limit ROWS ONLY;
END;
GO

CREATE OR ALTER PROC usp_FetchInvoices_count
	@SearchTerm NVARCHAR(100) = '', -- Search by InvoiceID, Customer Name, or Phone Number
    @BranchID INT = 0, -- Filter by BranchID
    @StartDate DATE = '', -- Filter by Start Date
    @EndDate DATE = '', -- Filter by End Date
	@Count INT OUT
AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @Search NVARCHAR(100) = '%' + @SearchTerm + '%';

    SELECT @Count = COUNT(DISTINCT i.InvoiceID)
    FROM Invoice i
    JOIN [Order] o ON i.OrderID = o.OrderID
    JOIN Customer c ON o.CustID = c.CustID
    LEFT JOIN OrderDetails od ON o.OrderID = od.OrderID
    WHERE 
        (CAST(i.InvoiceID AS NVARCHAR) LIKE @Search 
         OR c.CustName LIKE @Search 
         OR CAST(c.CustPhoneNumber AS NVARCHAR) LIKE @Search)
        AND (@BranchID = 0 OR o.BranchID = @BranchID)
        AND (@StartDate = '' OR i.InvoiceDate >= @StartDate)
        AND (@EndDate = '' OR i.InvoiceDate <= @EndDate)
END;

GO
-- 
-- 6. Fetch Customers
-- - Fetch customers with pagination. 
-- - Search by Name or CustomerID (?). 
CREATE OR ALTER PROC usp_FetchCustomers
    @Page INT = 1,
    @Limit INT = 18,
    @SearchTerm NVARCHAR(100) = '' 
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Offset INT = (@Page - 1) * @Limit;
    DECLARE @Search NVARCHAR(100) = @SearchTerm + '%';

    SELECT 
        CustID,
        CustName,
        CustEmail,
        CustGender,
        CustPhoneNumber,
        CustCitizenID
    FROM Customer
    WHERE (CAST(CustID AS NVARCHAR) LIKE @Search OR CustName LIKE @Search)
    ORDER BY (SELECT 1)
	OFFSET @Offset ROWS FETCH NEXT @Limit ROWS ONLY;
END;
GO

CREATE OR ALTER PROC usp_FetchCustomers_count
    @SearchTerm NVARCHAR(100) = '',
	@Count INT OUT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Search NVARCHAR(100) = @SearchTerm + '%';

    SELECT 
        @Count = COUNT(DISTINCT CustID)
    FROM Customer
    WHERE (CAST(CustID AS NVARCHAR) LIKE @Search OR CustName LIKE @Search)
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
    DECLARE @Search NVARCHAR(100) = @SearchTerm + '%';

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

CREATE OR ALTER PROC usp_FetchCoupons_count
    @SearchTerm NVARCHAR(100) = '', -- Search by CouponID or CouponCode
    @MinPurchase DECIMAL(19,4) = 0, -- Filter by MinPurchase
    @MinMembershipRequirement INT = NULL, -- Filter by MembershipRequirement
    @Status NVARCHAR(10) = '', -- Filter by Status ('available' or 'unavailable')
    @Count INT OUT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Search NVARCHAR(100) = @SearchTerm + '%';

    SELECT 
        @Count = COUNT(distinct CouponID)
    FROM Coupon
    WHERE (CAST(CouponID AS NVARCHAR) LIKE @Search OR CouponCode LIKE @Search)
        AND (MinPurchase >= @MinPurchase)
        AND (@MinMembershipRequirement IS NULL OR MinMembershipRequirement = @MinMembershipRequirement)
        AND (@Status = '' OR 
             (@Status = 'available' AND GETDATE() BETWEEN EffectiveDate AND ExpiryDate) OR
             (@Status = 'unavailable' AND GETDATE() NOT BETWEEN EffectiveDate AND ExpiryDate));
END;
GO


-- 8. Fetch statistics for a branch
-- - (e.g., number of orders, revenue, online accesss) I have no idea 

-- 9. Add Reservation
GO
CREATE OR ALTER PROCEDURE usp_InsertReservation
    @NumOfGuests INT,
    @ArrivalDateTime DATETIME,
    @RsNotes NVARCHAR(2047),
    @BranchID INT,
    @CustID INT,

	@rsId INT OUT
AS
SET XACT_ABORT, NOCOUNT ON
BEGIN TRY
	BEGIN TRANSACTION;

	IF (@ArrivalDateTime <= GETDATE())
		THROW 51000, 'Arrival date time must be after current date time', 1;

    INSERT INTO Reservation (NumOfGuests, ArrivalDateTime, RsNotes, BranchID, CustID)
    VALUES (@NumOfGuests, @ArrivalDateTime, @RsNotes, @BranchID, @CustID);

    -- Return the generated ID
    SELECT @rsId = SCOPE_IDENTITY();
	COMMIT TRANSACTION
END TRY
BEGIN CATCH
	IF @@trancount > 0 ROLLBACK TRANSACTION;
	;THROW
END CATCH
GO


-- 10. Add Delivery Order
GO
CREATE OR ALTER PROCEDURE usp_InsertDeliveryOrder
    @CustID INT = NULL,
    @BranchID INT,
	@DeliveryAddress NVARCHAR(2047),
	@DeliveryDateTime DATETIME,

	@OrderID INT OUT
AS
SET XACT_ABORT, NOCOUNT ON
BEGIN TRY
	BEGIN TRANSACTION;

	IF (@DeliveryDateTime <= GETDATE())
		THROW 51000, 'Delivery date time must be after current date time', 1;

    INSERT INTO [Order] (OrderStatus, CustID, BranchID, OrderType)
    VALUES ('UNVERIFIED', @CustID, @BranchID, 'D');

	-- Return the generated ID
    SELECT @OrderID = SCOPE_IDENTITY();
    
	INSERT INTO DeliveryOrder (OrderID, DeliveryAddress, DeliveryDateTime)
	VALUES (SCOPE_IDENTITY(), @DeliveryAddress, @DeliveryDateTime)

	COMMIT TRANSACTION;
END TRY
BEGIN CATCH
	IF @@trancount > 0 ROLLBACK TRANSACTION;
	;THROW
END CATCH
GO

DECLARE @OrderID INT;
EXEC usp_InsertDeliveryOrder 
    @CustID = 1,
    @BranchID = 1,
    @DeliveryAddress = '123 Main Street',
    @DeliveryDateTime = '2025-01-03 10:00:00',
	@OrderID = @OrderID OUTPUT; 

-- 11. Add Dine-In Order
GO
CREATE OR ALTER PROCEDURE usp_InsertDineInOrder
    @CustID INT = NULL,
    @BranchID INT,
	@StaffID INT = NULL, -- Determine an order is created by a staff or by a customer through reservation
	@TableCode INT,
	@RsID INT = NULL,

	@OrderID INT OUT
AS
SET XACT_ABORT, NOCOUNT ON
BEGIN TRY
	BEGIN TRANSACTION;
	
	IF (@StaffID IS NOT NULL) -- If created by a staff
	BEGIN
		IF NOT EXISTS (
			SELECT 1 
			FROM [Table] 
			WHERE BranchID = @BranchID 
				AND TableCode = @TableCode
				AND isVacant = 1
		) THROW 51000, 'The table does not exists or it is being occupied at the moment', 1;

		INSERT INTO [Order] (OrderStatus, StaffID, CustID, BranchID, OrderType)
		VALUES ('VERIFIED', @StaffID, @CustID, @BranchID, 'I');

		-- Return the generated ID
		SELECT @OrderID = SCOPE_IDENTITY();

		INSERT INTO DineInOrder (OrderID, TableCode, BranchID, RsID)
		VALUES (SCOPE_IDENTITY(), @TableCode, @BranchID, @RsID);

		-- Update table status
		UPDATE [Table] WITH (UPDLOCK, HOLDLOCK)
		SET isVacant = 0
		WHERE BranchID = @BranchID AND TableCode = @TableCode
	END
	ELSE -- If created by a customer through reservation
	BEGIN
		IF (@RsID IS NULL)
			THROW 51000, 'Pre-order made by customer must include reservation ID', 1;

		INSERT INTO [Order] (OrderStatus, CustID, BranchID, OrderType)
		VALUES ('UNVERIFIED', @CustID, @BranchID, 'D');

		-- Return the generated ID
		SELECT @OrderID = SCOPE_IDENTITY();

		INSERT INTO DineInOrder (OrderID, BranchID, RsID)
		VALUES (SCOPE_IDENTITY(), @BranchID, @RsID);
	END

	COMMIT TRANSACTION;
END TRY
BEGIN CATCH
	IF @@trancount > 0 ROLLBACK TRANSACTION;
	;THROW
END CATCH
GO

-- 1011.1 Add Order Details
GO
CREATE OR ALTER PROCEDURE usp_InsertOrderDetails
    @OrderID INT,
	@ItemID INT,
	@Quantity INT
AS
SET XACT_ABORT, NOCOUNT ON
BEGIN TRY
	BEGIN TRANSACTION;
	
	DECLARE @OrderType CHAR(1), @BranchID INT, @OrderStatus CHAR(10);
	SELECT 
		@OrderType = OrderType, 
		@BranchID = BranchID,
		@OrderStatus = OrderStatus
	FROM [Order] 
	WHERE OrderID = @OrderID;

	-- Check if Order is not cancelled or completed
	IF (@OrderStatus IN ('CANCELLED', 'COMPLETED'))
		THROW 51000, 'Order must not be cancelled or completed', 1;

	-- Check if that branch serve that item
	DECLARE @IsShippable BIT = IIF (EXISTS(
		SELECT bmi.IsShippable
		FROM BranchMenuItem bmi
		WHERE 
			bmi.ItemID = @ItemID
			AND bmi.BranchID = @BranchID
	), 1, 0);

	IF (@IsShippable IS NULL)
		THROW 51000, 'This item is not for sale at this branch', 1;
	IF (@OrderType = 'D' AND @IsShippable = 0)
		THROW 51000, 'This item cannot be placed on delivery order', 1;

	DECLARE @UnitPrice DECIMAL(19,4) = (SELECT UnitPrice FROM MenuItem WHERE @ItemID = ItemID);
	INSERT INTO OrderDetails (OrderID, ItemID, UnitPrice, Quantity)
	VALUES (@OrderID, @ItemID, @UnitPrice, @Quantity);

	COMMIT TRANSACTION;
END TRY
BEGIN CATCH
	IF @@trancount > 0 ROLLBACK TRANSACTION;
	;THROW
END CATCH
GO

-- 1011.2 Update table state when delete dine in order
GO
CREATE OR ALTER TRIGGER trg_UpdateTableVacancy ON DineInOrder
AFTER DELETE, UPDATE
AS
BEGIN
	-- If no rows affected
	IF (ROWCOUNT_BIG() = 0) RETURN;

	-- Update vacancy where table is no longer occupied by deletion
	UPDATE [Table] SET isVacant = 1
	FROM [Table] t, deleted d
	WHERE t.BranchID = d.BranchID AND t.TableCode = d.TableCode

	-- Update vacancy where table is no longer occupied by cancelling order
	UPDATE [Table] SET isVacant = 1
	FROM [Table] t, inserted i, [Order] o
	WHERE i.OrderID = o.OrderID
		AND t.BranchID = i.BranchID 
		AND t.TableCode = i.TableCode
		AND o.OrderStatus = N'CANCELLED'
END
GO

-- 12. Create Invoice from order  
-- Issue an Invoice
-- OrderID as a parameter. Change OrderStatus to Completed
-- Add new Invoice.
-- Update card points (if exists)
-- Check and upgrade card if conditions are met
-- Mark table as "free".
GO
CREATE OR ALTER PROCEDURE usp_CreateInvoice (
    @OrderID INT,
	@PaymentMethod NVARCHAR(50),
	@TaxRate DECIMAL(4,3) = 0.1,
	@CouponCode VARCHAR(50),

	@InvoiceID INT OUT
)
AS
SET XACT_ABORT, NOCOUNT ON
BEGIN TRY
	BEGIN TRANSACTION;
	DECLARE @DiscountAmount DECIMAL(19,4) = 0,
			@DiscountRate DECIMAL(4,3),
            @ShippingCost DECIMAL(19,4),
            @CardTypeDiscountRate DECIMAL(4,3) = 0.0,
			@OrderStatus CHAR(10),  
			@BranchID INT,
			@Subtotal DECIMAL(19,4),
			@Total DECIMAL(19,4),
			@CardType INT,
			@CustID INT,
			@CouponID INT;
	SELECT @CouponID = CouponID FROM Coupon WHERE CouponCode = @CouponCode
	 -- Check if the order exists
	 IF NOT EXISTS (
		SELECT 1 FROM [Order] WHERE OrderID = @OrderID
	 )
		THROW 51000, 'Order does not exist.', 1;

	 SELECT @CustID = CustID FROM [Order] WHERE OrderID = @OrderID;
            
	 SELECT @Subtotal = SUM(UnitPrice * Quantity)
	 FROM OrderDetails
	 WHERE OrderID = @OrderID;

	 -- Check current Order status
     SELECT @OrderStatus = OrderStatus, @BranchID = BranchID
	 FROM [Order]
     WHERE OrderID = @OrderID;

	-- If the order is already completed or cancelled
    IF @OrderStatus IN ('COMPLETED', 'CANCELLED')
        THROW 51000, 'Order is already completed or cancelled', 1;

	-- Get the OrderType to determine the ShippingCost
    DECLARE @OrderType CHAR(1);
    SELECT @OrderType = OrderType FROM [Order] WHERE OrderID = @OrderID;

    IF @OrderType = 'I'
    BEGIN
        SET @ShippingCost = 0;
    END
    ELSE 
    BEGIN
        SET @ShippingCost = 30000;
    END

	-- Check coupon
     IF @CouponID IS NOT NULL
     BEGIN
        IF NOT EXISTS (
            SELECT 1
            FROM Coupon c
            WHERE c.CouponID = @CouponID
                AND c.ExpiryDate >= GETDATE()
                AND c.RemainingUsage > 0
                AND c.MinPurchase <= @Subtotal
                AND c.MinMembershipRequirement <= (
                    SELECT mc.CardType
                    FROM MembershipCard mc
                    INNER JOIN [Order] o ON mc.CustID = o.CustID
                    WHERE o.OrderID = @OrderID
                )
        )
        BEGIN
            ;THROW 51000, 'The coupon is invalid or does not meet the requirements', 1;
        END

        SELECT @DiscountAmount = 
            CASE 
                WHEN c.DiscountFlat IS NOT NULL THEN c.DiscountFlat
                WHEN c.DiscountRate IS NOT NULL THEN 
                    CASE 
                        WHEN (c.DiscountRate * @Subtotal) > c.MaxDiscount THEN c.MaxDiscount
                        ELSE c.DiscountRate * @Subtotal
                    END
                ELSE 0 
            END
        FROM Coupon c
        WHERE c.CouponID = @CouponID;

		-- Deduct the coupon usage
        UPDATE Coupon SET RemainingUsage = RemainingUsage - 1 WHERE CouponID = @CouponID;
    END

	-- Get the CardType discount rate if the customer has a membership card
    SELECT @CardTypeDiscountRate = ct.DiscountRate, @CardType = mc.CardType
    FROM MembershipCard mc
    INNER JOIN CardType ct ON mc.CardType = ct.CardTypeID
    WHERE mc.CustID = @CustID
	
    -- Insert Invoice 
    INSERT INTO Invoice (OrderID, Subtotal, DiscountRate, TaxRate, ShippingCost, PaymentMethod, InvoiceDate, CouponID, BranchID, CustID)
    VALUES (@OrderID, @Subtotal, @CardTypeDiscountRate, @TaxRate, @ShippingCost, @PaymentMethod, GETDATE(), @CouponID, @BranchID, @CustID);

	SET @InvoiceID = SCOPE_IDENTITY();

	SET @Total = (@Subtotal * (1 - @CardTypeDiscountRate)  - @DiscountAmount + @ShippingCost) * ( 1 + @TaxRate)

    -- Update Membership Card points if exists
    UPDATE MembershipCard 
    SET Points = Points + FLOOR(@Total / 100000)
    WHERE CustID = @CustID

	-- Check and upgrade membership card type if conditions are met
	IF @CardType IN (1,2)
	BEGIN
		-- Get the required points for upgrading to the next card type
		DECLARE @NextCardType INT = @CardType + 1;
		DECLARE @PointsRequiredForUpgrade INT = (SELECT PointsRequiredForUpgrade FROM CardType WHERE CardTypeID = @NextCardType);

		IF (SELECT Points FROM MembershipCard WHERE CustID = @CustID) >= @PointsRequiredForUpgrade
		BEGIN
			-- Upgrade the membership card type if sufficient points are available
			UPDATE MembershipCard
			SET CardType = @NextCardType, Points = 0, IssuedDate = GETDATE()
			WHERE CustID = @CustID;
		END
	END

	-- Mark the table as "free" if the order is for dine-in
	UPDATE [Table]
    SET isVacant = 1                  
    WHERE EXISTS (
        SELECT 1 
        FROM DineInOrder dio
        WHERE dio.OrderID = @OrderID 
            AND [Table].BranchID = dio.BranchID 
            AND [Table].TableCode = dio.TableCode
    );
	-- Update SoldQuantity for each MenuItem
	UPDATE MenuItem
	SET SoldQuantity = SoldQuantity + od.Quantity
	FROM MenuItem mi
		INNER JOIN OrderDetails od ON mi.ItemID = od.ItemID
	WHERE od.OrderID = @OrderID;

	-- Update OrderStatus to Completed
    UPDATE [Order] SET OrderStatus = 'COMPLETED' WHERE OrderID = @OrderID;

	COMMIT TRANSACTION;
END TRY
BEGIN CATCH
	IF @@trancount > 0 ROLLBACK TRANSACTION;
	;THROW
END CATCH
GO

--UPDATE Coupon
--SET ExpiryDate = '20251231'
--WHERE CouponID = 1


--EXEC usp_CreateInvoice 
--    @OrderID = 1, 
--    @PaymentMethod = N'Credit Card', 
--    @CouponID = 2;

--select * from Customer
--select * from [Order]
--select * from OrderDetails
--select * from MembershipCard
--select * from Coupon
--select * from CardType
--select * from [Table]
--select * from DineInOrder


--select * from Invoice

go
-- 13. Add new staff
CREATE OR ALTER PROC InsertStaff
    @DeptName VARCHAR(10),
    @BranchID INT,
    @StaffName NVARCHAR(100),
    @StaffDOB DATE,
    @StaffGender CHAR(1)            
AS
SET XACT_ABORT, NOCOUNT ON
BEGIN TRY
    BEGIN TRANSACTION;

	-- Retrieve the new StaffID
    DECLARE @NewStaffID INT = SCOPE_IDENTITY();

    -- Insert into Staff table
    INSERT INTO Staff (StaffID, DeptName, BranchID)
    VALUES (@NewStaffID, @DeptName, @BranchID);

    -- Insert into StaffInfo table
    INSERT INTO StaffInfo (StaffID, StaffName, StaffDOB, StaffGender)
    VALUES (@NewStaffID, @StaffName, @StaffDOB, @StaffGender);

	-- Return the generated ID
    SELECT SCOPE_IDENTITY();

    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
GO


-- 14. Transfer staff
-- Update BranchID, Department.
-- Add work history.

CREATE OR ALTER PROC TransferStaff
    @StaffID INT,         
    @NewBranchID INT,     
    @NewDeptName VARCHAR(10), 
    @StartDate DATETIME
AS
SET XACT_ABORT, NOCOUNT ON
BEGIN TRY
    BEGIN TRANSACTION;

	-- Validate that the new BranchID and DeptName exist in the Department table
	IF NOT EXISTS (
		SELECT 1 
		FROM Department
		WHERE BranchID = @NewBranchID AND DeptName = @NewDeptName
	)
	BEGIN
		;THROW 51000, 'Invalid BranchID or DeptName.', 1;
	END

	DECLARE @CurrentBranchID INT, @CurrentDeptName VARCHAR(10);

	SELECT 
		@CurrentBranchID = BranchID,
		@CurrentDeptName = DeptName
	FROM Staff
	WHERE StaffID = @StaffID;

	-- If there is no change in branch or department
	IF @CurrentBranchID = @NewBranchID AND @CurrentDeptName = @NewDeptName
	BEGIN
		RETURN;
	END

	SET @StartDate = GETDATE();

	-- Update the QuitDate of the current work history
    UPDATE WorkHistory
    SET QuitDate = @StartDate
    WHERE StaffID = @StaffID 
		AND BranchID = @CurrentBranchID 
		AND DeptName = @CurrentDeptName
        AND QuitDate IS NULL;

	-- Add a new record to the WorkHistory table for the new department
	INSERT INTO WorkHistory (StaffID, StartDate, BranchID, DeptName)
	VALUES (
		@StaffID,
		@StartDate,
		@NewBranchID,
		@NewDeptName
	);

	-- Update the BranchID and DeptName in the Staff table
	UPDATE Staff
	SET 
		BranchID = @NewBranchID,
		DeptName = @NewDeptName
	WHERE StaffID = @StaffID;

	COMMIT TRANSACTION;
END TRY
BEGIN CATCH
	IF @@trancount > 0 ROLLBACK TRANSACTION;
	;THROW
END CATCH;
GO

--select * from Coupon
-- 15. Update membership cards based on retention, downgrade, and reset conditions
GO
CREATE OR ALTER PROCEDURE UpdateMembershipCards
AS
BEGIN
    SET XACT_ABORT, NOCOUNT ON
	BEGIN TRY
		BEGIN TRANSACTION;
		DECLARE @CurrentDate DATE = GETDATE();
		-- Retain current tier if customers meet renewal requirements
		UPDATE MC
		SET 
			Points = CASE 
						WHEN CT.CardTypeID IN (2, 3) THEN 0 
						ELSE MC.Points -- Retain points for Membership
					 END,
			IssuedDate = @CurrentDate
		FROM MembershipCard MC
			JOIN CardType CT ON MC.CardType = CT.CardTypeID
		WHERE 
			DATEDIFF(YEAR, MC.IssuedDate, @CurrentDate) >= 1 
			AND MC.Points >= CT.PointsRequiredForRenewal 
			AND MC.Points < CT.PointsRequiredForUpgrade;
		-- Reset to Membership tier if the card has no activity for a year
		UPDATE MC
		SET 
			MC.CardType = 1,
			Points = 0, 
			IssuedDate = @CurrentDate
		FROM MembershipCard MC
		WHERE 
			DATEDIFF(YEAR, MC.IssuedDate, @CurrentDate) >= 1 
			AND MC.Points = 0;
		-- Downgrade cards if customers do not meet renewal requirements
		UPDATE MC
		SET 
			MC.CardType = CTPrev.CardTypeID, 
			Points = 0, 
			IssuedDate = @CurrentDate 
		FROM MembershipCard MC
			JOIN CardType CT ON MC.CardType = CT.CardTypeID
			JOIN CardType CTPrev ON CTPrev.CardTypeID = CT.CardTypeID - 1 
		WHERE 
			DATEDIFF(YEAR, MC.IssuedDate, @CurrentDate) >= 1 
			AND MC.Points < CT.PointsRequiredForRenewal; 
		COMMIT TRANSACTION;
	END TRY
	BEGIN CATCH
		IF @@trancount > 0 ROLLBACK TRANSACTION;
		;THROW
	END CATCH
END;
GO

--16. Thống kê doanh thu theo ngày/tháng/quý/năm cho một chi nhánh cụ thể hoặc cho toàn công ty.
CREATE OR ALTER PROCEDURE usp_GetRevenueStats
    @BranchID INT = 0,       -- 0 = all
    @GroupBy NVARCHAR(10) = 'day' -- 'day', 'month', 'quarter', 'year'
AS
BEGIN
    SET NOCOUNT ON;

    IF @GroupBy NOT IN ('day', 'month', 'quarter', 'year')
    BEGIN
        RAISERROR('Invalid value for @GroupBy. Valid options are day, month, quarter, year.', 16, 1);
        RETURN;
    END

    -- Get range
    DECLARE @StartDate DATE, @EndDate DATE;
    SET @EndDate = GETDATE();

    IF @GroupBy = 'day'
    BEGIN
        SET @StartDate = DATEFROMPARTS(YEAR(@EndDate), MONTH(@EndDate), 1); -- Day 1 of current month
    END
    ELSE IF @GroupBy = 'month'
    BEGIN
        SET @StartDate = DATEFROMPARTS(YEAR(@EndDate), 1, 1); -- 1/1/x
    END
    ELSE IF @GroupBy = 'quarter'
    BEGIN
        SET @StartDate = DATEFROMPARTS(YEAR(@EndDate), 1, 1); -- 1/1/x
    END
    ELSE IF @GroupBy = 'year'
    BEGIN
        SET @StartDate = '1900-01-01'; -- All data
    END

    -- Return format
    DECLARE @GroupByFormat NVARCHAR(20);
    SET @GroupByFormat = 
        CASE @GroupBy
            WHEN 'day' THEN 'yyyy-MM-dd'
            WHEN 'month' THEN 'yyyy-MM'
            WHEN 'quarter' THEN 'yyyy-QQ'
            WHEN 'year' THEN 'yyyy'
        END;

    -- Magic
    SELECT 
        FORMAT(InvoiceDate, @GroupByFormat) AS RevenuePeriod,
        ISNULL(@BranchID, 0) AS Branch,
        SUM(Subtotal - (Subtotal * DiscountRate / 100) + ShippingCost + (Subtotal * TaxRate / 100)) AS TotalRevenue
    FROM Invoice
    WHERE 
        (BranchID = @BranchID OR @BranchID = 0) AND
        (InvoiceDate >= @StartDate AND InvoiceDate <= @EndDate)
    GROUP BY 
        FORMAT(InvoiceDate, @GroupByFormat)
    ORDER BY 
        RevenuePeriod;
END;
GO

--17. Thống kê doanh thu theo từng món, món chạy nhất trong khoảng cụ thể theo chi nhánh, khu vực.
CREATE OR ALTER PROCEDURE usp_GetItemSalesStats
    @BranchID INT = 0,          -- 0 for all branches
    @Region NVARCHAR(50) = '',   -- '' for all regions, N'Thành Phố Hồ Chính Minh', N'Hà Nội'
    @TimePeriod INT = 0,            -- Days. 0: All time
	@SortDirection BIT = 0
AS
BEGIN
    SET NOCOUNT ON;

    -- Validate TimePeriod input
    IF @TimePeriod < 0
    BEGIN
        RAISERROR('Invalid value for @TimePeriod.', 16, 1);
        RETURN;
    END

    -- Determine date range based on TimePeriod
    DECLARE @StartDate DATE, @EndDate DATE;
    SET @EndDate = GETDATE();

    IF @TimePeriod = 0
        SET @StartDate = '1900-01-01'; -- All time
    ELSE
        SET @StartDate = DATEADD(DAY, -@TimePeriod, @EndDate); -- Subtract days based on @TimePeriod

    -- Calculate item sales statistics
	SELECT 
	    MI.ItemID,
	    MI.ItemName,
	    ISNULL(@BranchID, 0) AS Branch,
	    SUM(OD.Quantity) AS TotalSoldQuantity,
	    SUM(OD.Quantity * OD.UnitPrice) AS TotalRevenue
	FROM OrderDetails OD
	JOIN MenuItem MI ON OD.ItemID = MI.ItemID
	JOIN [Order] O ON OD.OrderID = O.OrderID
	JOIN Branch B ON B.BranchID = O.BranchID
    WHERE 
        (O.BranchID = @BranchID OR @BranchID = 0) AND
        (B.BranchRegion = @Region OR @Region ='') AND
        (O.OrderDateTime >= @StartDate AND O.OrderDateTime <= @EndDate)
    GROUP BY 
        MI.ItemID, MI.ItemName
    ORDER BY TotalRevenue DESC
END;
GO

GO
-- 18. Ghi lại lịch sử truy cập trên web của khách hàng
CREATE OR ALTER PROCEDURE usp_LogCustomerOnlineAccess (
	@CustID INT,
	@IsStart BIT,

	@AccessID INT OUT
)
AS
BEGIN
BEGIN TRY
	SET XACT_ABORT, NOCOUNT ON;
	
	IF (@IsStart = 1) 
	BEGIN
		INSERT INTO OnlineAccess (CustID, StartDateTime, EndDateTime)
		VALUES (@CustID, GETDATE(), NULL);
		SET @AccessID = SCOPE_IDENTITY();
	END
	ELSE
	BEGIN
		SET @AccessID = (
			SELECT TOP 1 AccessID 
			FROM OnlineAccess 
			WHERE EndDateTime IS NULL AND CustID = @CustID
			ORDER BY StartDateTime
		)
		UPDATE OnlineAccess SET EndDateTime = GETDATE()
		WHERE AccessID = @AccessID;
	END

	COMMIT TRANSACTION;
END TRY
BEGIN CATCH
	IF @@trancount > 0 ROLLBACK TRANSACTION;
	;THROW
END CATCH
END
GO

GO

-- 19. Nhân viên xác nhận đơn đặt món hoặc hủy đơn
CREATE OR ALTER PROCEDURE usp_updateOrderStatus (
	@OrderID INT,
	@StaffID INT,
	@OrderStatus CHAR(10)
)
AS
BEGIN
BEGIN TRY
	SET XACT_ABORT, NOCOUNT ON;
	
	DECLARE @CurrentOrderStatus CHAR(10) = (
		SELECT OrderStatus FROM [Order] WITH (UPDLOCK, HOLDLOCK) WHERE OrderID = @OrderID)
	IF (@CurrentOrderStatus = @OrderStatus)
		THROW 51000, N'There was no change in order status', 1;
	IF (@CurrentOrderStatus IN ('COMPLETED', 'CANCELLED'))
		THROW 51000, N'Order status cannot be changed anymore since it is already completed or cancelled', 1;
	IF (@OrderStatus = 'UNVERIFIED')
		THROW 51000, N'Staff cannot set order to unverified', 1;
	IF (@OrderStatus = 'COMPLETED')
		THROW 51000, N'Order cannot be completed manually but through creating invoice', 1;
	IF (@CurrentOrderStatus = 'SERVED' AND @OrderStatus != 'CANCELLED')
		THROW 51000, N'Served order cannot be revert back to previous status', 1;
	
	UPDATE [Order] SET OrderStatus = @OrderStatus
	WHERE OrderID = @OrderID;

	COMMIT TRANSACTION;
END TRY
BEGIN CATCH
	IF @@trancount > 0 ROLLBACK TRANSACTION;
	;THROW
END CATCH
END
GO

--select * from MembershipCard
--select * from Customer
--select * from Staff
--select * from CardType
--INSERT INTO MembershipCard (IssuedDate, CardType, CustID, StaffID, Points)
--VALUES 
--     Case 1: Silver card, 70 points, Eligible for renewal
--	('2023-12-30', 2, 1, 1, 70),
--     Case 2: Gold card, 50 points, Not eligible for renewal -> downgrade
--	('2023-12-30', 3, 2, 2, 50),
--     Case 3: Gold card, 0 points (no activity for a year) -> reset to Member
--	('2023-12-30', 3, 5, 2, 0);
--GO
--EXEC UpdateMembershipCards
--select * from MembershipCard
USE msdb;
GO
-- Delete job if it already exists
IF EXISTS(SELECT job_id FROM msdb.dbo.sysjobs WHERE (name = N'UpdateMembershipCards_Daily'))
BEGIN
    EXEC msdb.dbo.sp_delete_job
        @job_name = N'UpdateMembershipCards_Daily';
END
EXEC sp_add_job 
    @job_name = N'UpdateMembershipCards_Daily',  
    @enabled = 1,                               
    @description = N'Run UpdateMembershipCards procedure daily.', 
    @start_step_id = 1;                       
EXEC sp_add_jobstep 
    @job_name = N'UpdateMembershipCards_Daily', 
    @step_name = N'Execute UpdateMembershipCards',  
    @subsystem = N'TSQL',                          
    @command = N'EXEC UpdateMembershipCards;',     
    @database_name = N'SushiX',          
    @on_success_action = 1;                        
EXEC sp_add_jobschedule 
    @job_name = N'UpdateMembershipCards_Daily', 
    @name = N'Daily',                    
    @freq_type = 4,    
	@freq_interval = 1,
    @active_start_time = '230000';      -- start time 23:00:00                 
EXEC sp_add_jobserver 
    @job_name = N'UpdateMembershipCards_Daily', 
    @server_name = @@servername;                   
GO

USE SushiX
GO
-- Check the job history for 'UpdateMembershipCards_Daily'
--SELECT 
--    job_id,
--    step_id,
--    step_name,
--    run_status,
--    run_date,
--    run_duration,
--    message
--FROM 
--    dbo.sysjobhistory
--WHERE 
--    job_id = (SELECT job_id FROM msdb.dbo.sysjobs WHERE name = N'UpdateMembershipCards_Daily')
--ORDER BY 
--    run_date DESC, run_duration DESC;
--GO
-- Check the schedule for the job
--SELECT 
--    s.schedule_id, 
--    s.name AS schedule_name, 
--    s.freq_type, 
--    s.freq_interval, 
--    s.active_start_time
--FROM 
--    msdb.dbo.sysschedules s
--    INNER JOIN msdb.dbo.sysjobschedules js ON s.schedule_id = js.schedule_id
--WHERE 
--    js.job_id = (SELECT job_id FROM msdb.dbo.sysjobs WHERE name = N'UpdateMembershipCards_Daily');
--GO
--EXEC sp_start_job @job_name = N'UpdateMembershipCards_Daily';
--GO