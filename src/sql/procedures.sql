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
		and (@BranchID = 0 or bmi.BranchID = @CategoryID)
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
create or alter proc usp_CountItems
    @SearchTerm nvarchar(100) = '', -- ItemID or ItemName
    @CategoryID int = 0, -- Filter
    @BranchID int = 0, -- Filter
	@FilterShippable bit = 0, --0: no filter, 1: yes XD
	@count int out
as
begin
	SET NOCOUNT ON;

    declare @Search nvarchar(100) = '%' + @SearchTerm + '%';

	select @count = count(distinct(mi.ItemID))
	from MenuItem mi left join BranchMenuItem bmi on (bmi.ItemID = mi.ItemID and @BranchID != 0)
	where (mi.ItemName like @Search or mi.ItemID like @Search)
		and (@CategoryID = 0 or mi.CategoryID = @CategoryID)
		and (@BranchID = 0 or bmi.BranchID = @BranchID)
		and (@FilterShippable = 0 or bmi.IsShippable = 1)
end;
GO

-- 2. Fetch Staffs
-- Fetch staffs with pagination. 
-- Search by StaffName and StaffID. 
-- Filter by BranchID, Deparment.
--CREATE OR ALTER PROC usp_FetchStaffs
--	@Page int = 1,
--	@Limit int = 18,
--	@SearchTerm nvarchar(100) = '', -- StaffID/StaffName
--	@BranchID int = 0, --Filter
--	@Department varchar(10) = ''
--AS
--BEGIN
--	SET NOCOUNT ON;

--	DECLARE @Offset int = (@Page - 1) * @Limit;
--	DECLARE @Search nvarchar(100) = '%' + @SearchTerm + '%';

--	SELECT s.StaffID,
--	s.StaffName,
--	s.StaffDOB,
--	s.StaffGender,
--	s.DeptName,
--	s.BranchID,
--	s.isBranchManager
--	FROM Staff s
--	WHERE s.StaffID like @Search or s.StaffName like @Search
--		AND (@BranchID = 0 OR s.BranchID = @BranchID)
--		AND (@Department = '' OR s.DeptName = @Department)
--	ORDER BY s.StaffID,
--	s.StaffName,
--	s.StaffDOB,
--	s.StaffGender,
--	s.DeptName,
--	s.BranchID,
--	s.isBranchManager
--	OFFSET @Offset ROWS FETCH NEXT @Limit ROWS ONLY;
--END
--GO

--EXEC sp_FetchStaffs

--GO

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
	ORDER BY 
    CASE WHEN @SortDirection = 0 THEN r.RsDateTime END,
    CASE WHEN @SortDirection = 1 THEN r.RsDateTime END DESC
	OFFSET @Offset ROWS FETCH NEXT @Limit ROWS ONLY;
END
GO

CREATE OR ALTER PROC usp_CountReservation
	@SearchTerm NVARCHAR(100) = '', -- ReservationID
	@BranchID INT = 0 -- Filter
AS
BEGIN
	SET NOCOUNT ON;
	DECLARE @Search NVARCHAR(100) = @SearchTerm + '%';

	SELECT COUNT(DISTINCT(r.RsID))
	FROM Reservation r
	JOIN Customer c ON r.CustID = c.CustID
	WHERE (r.RsID LIKE @Search)
		AND (@BranchID = 0 OR r.BranchID = @BranchID)
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
	@OrderID INT = 0, -- Filter by Order
	@OrderStatus NVARCHAR(50) = '', -- Filter by 
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
		c.CustName,
		c.CustPhoneNumber,
		c.CustEmail,
		CASE 
			WHEN do.OrderID IS NOT NULL THEN 'Delivery'
			WHEN dio.OrderID IS NOT NULL THEN 'Dine-In'
			ELSE 'Unknown'
		END AS OrderType,
		ISNULL(SUM(od.UnitPrice * od.Quantity), 0) AS EstimatedPrice
	FROM [Order] o
	JOIN Customer c ON o.CustID = c.CustID
	LEFT JOIN DeliveryOrder do ON o.OrderID = do.OrderID
	LEFT JOIN DineInOrder dio ON o.OrderID = dio.OrderID
	LEFT JOIN OrderDetails od ON o.OrderID = od.OrderID
	WHERE (o.OrderID LIKE @Search) OR (c.CustName LIKE @Search) 
        OR (CAST(c.CustPhoneNumber AS NVARCHAR) LIKE @Search)
		AND (@BranchID = 0 OR o.BranchID = @BranchID)
		AND (@OrderID = 0 or o.OrderID = @OrderID)
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
exec usp_FetchOrders
GO
CREATE OR ALTER PROC usp_CountOrders
	@SearchTerm NVARCHAR(100) = '', -- Search by OrderID
	@BranchID INT = 0, -- Filter by Branch
	@OrderStatus NVARCHAR(50) = '', -- Filter by OrderStatus
	@OrderType CHAR(1) = '' -- Filter by OrderType ('D' for DeliveryOrder, 'I' for DineInOrder)
AS
BEGIN
	SET NOCOUNT ON;
	DECLARE @Search NVARCHAR(100) = @SearchTerm + '%';

	SELECT COUNT(DISTINCT o.OrderID)
	FROM [Order] o
	JOIN Customer c ON o.CustID = c.CustID
	LEFT JOIN DeliveryOrder do ON o.OrderID = do.OrderID
	LEFT JOIN DineInOrder dio ON o.OrderID = dio.OrderID
	LEFT JOIN OrderDetails od ON o.OrderID = od.OrderID
	WHERE (o.OrderID LIKE @Search) OR (c.CustName LIKE @Search) 
        OR (CAST(c.CustPhoneNumber AS NVARCHAR) LIKE @Search)
		AND (@BranchID = 0 OR o.BranchID = @BranchID)
		AND (@OrderStatus = '' OR o.OrderStatus = @OrderStatus)
		AND (@OrderType = '' OR 
			(@OrderType = 'Delivery' AND do.OrderID IS NOT NULL) OR 
			(@OrderType = 'Dine-In' AND dio.OrderID IS NOT NULL))
END


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
    @SearchTerm NVARCHAR(100) = '', -- Search by InvoiceID, Customer Name, or Phone Number
    @BranchID INT = 0, -- Filter by BranchID
    @StartDate DATE = NULL, -- Filter by Start Date
    @EndDate DATE = NULL, -- Filter by End Date
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
        ISNULL(SUM(od.UnitPrice * od.Quantity), 0) AS TotalPrice,
        c.CustName,
        c.CustPhoneNumber,
        c.CustEmail
    FROM Invoice i
    JOIN [Order] o ON i.OrderID = o.OrderID
    JOIN Customer c ON o.CustID = c.CustID
    LEFT JOIN OrderDetails od ON o.OrderID = od.OrderID
    WHERE 
        (CAST(i.InvoiceID AS NVARCHAR) LIKE @Search 
         OR c.CustName LIKE @Search 
         OR CAST(c.CustPhoneNumber AS NVARCHAR) LIKE @Search)
        AND (@BranchID = 0 OR o.BranchID = @BranchID)
        AND (@StartDate IS NULL OR i.InvoiceDate >= @StartDate)
        AND (@EndDate IS NULL OR i.InvoiceDate <= @EndDate)
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
            WHEN @SortDirection = 0 THEN ISNULL(SUM(od.UnitPrice * od.Quantity), 0)
        END ASC,
        CASE 
            WHEN @SortDirection = 1 THEN ISNULL(SUM(od.UnitPrice * od.Quantity), 0)
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
    @SearchTerm NVARCHAR(100) = '' 
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

SELECT * FROM DeliveryOrder
SELECT * FROM [Order]

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
		IF EXISTS (
			SELECT 1 
			FROM [Table] 
			WHERE BranchID = @BranchID 
				AND TableCode = @TableCode
				AND isVacant = 0
		) THROW 51000, 'The table is being occupied at the moment', 1;

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
	@CouponID INT
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
			@Subtotal DECIMAL(19,4),
			@Total DECIMAL(19,4),
			@CardType INT,
			@CustID INT;
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
     SELECT @OrderStatus = OrderStatus 
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
    INSERT INTO Invoice (OrderID, Subtotal, DiscountRate, TaxRate, ShippingCost, PaymentMethod, InvoiceDate, CouponID)
    VALUES (@OrderID, @Subtotal, @CardTypeDiscountRate, @TaxRate, @ShippingCost, @PaymentMethod, GETDATE(), @CouponID);

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
			SET CardType = @NextCardType
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
	-- Update OrderStatus to Completed
    UPDATE [Order] SET OrderStatus = 'COMPLETED' WHERE OrderID = @OrderID;

	COMMIT TRANSACTION;
END TRY
BEGIN CATCH
	IF @@trancount > 0 ROLLBACK TRANSACTION;
	;THROW
END CATCH
GO

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
    @IsBranchManager BIT = 0,    
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
    INSERT INTO Staff (StaffID, DeptName, BranchID, IsBranchManager)
    VALUES (@NewStaffID, @DeptName, @BranchID, @IsBranchManager);

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
    @StartDate DATE      
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
		;THROW 51000, 'BranchID and Department are unchanged.', 1;
	END

	-- Update the QuitDate of the current work history
    UPDATE WorkHistory
    SET QuitDate = @StartDate
    WHERE StaffID = @StaffID 
		AND DeptID = (SELECT DeptID FROM Department WHERE BranchID = @CurrentBranchID AND DeptName = @CurrentDeptName)
        AND QuitDate IS NULL;

	-- Add a new record to the WorkHistory table for the new department
	INSERT INTO WorkHistory (StaffID, StartDate, DeptID)
	VALUES (
		@StaffID,
		@StartDate,
		(SELECT DeptID FROM Department WHERE BranchID = @NewBranchID AND DeptName = @NewDeptName)
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
