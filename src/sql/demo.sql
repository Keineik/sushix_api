--1. Non-clustered Index cho MenuItem(ItemName) 
CREATE NONCLUSTERED INDEX IX_MenuItem_ItemName
ON MenuItem(ItemName);

DROP INDEX IX_MenuItem_ItemName ON MenuItem;

exec usp_FetchItems @SearchTerm = 'Bánh'

--2. Non-clustered Index cho Customer(CustName)
CREATE NONCLUSTERED INDEX IX_Customer_CustName
ON Customer(CustName);

DROP INDEX IX_Customer_CustName ON Customer;

exec usp_FetchCustomers @SearchTerm = 'Winne'

--3. Non-clustered Index cho Order(CustID)
CREATE NONCLUSTERED INDEX IX_Order_CustID
ON [Order](CustID);

DROP INDEX IX_Order_CustID ON [Order]

exec usp_FetchOrders @CustID = 1

--4. Non-clustered Index cho OrderDetails(ItemID)
CREATE NONCLUSTERED INDEX IX_OrderDetails_ItemID_Include_Quantity_UnitPrice
ON OrderDetails(ItemID) INCLUDE (Quantity, UnitPrice);

DROP INDEX IX_OrderDetails_ItemID_Include_Quantity_UnitPrice ON [OrderDetails]

--Cooked

-- 5. Non-clustered Index cho Invoice(CustID)
CREATE NONCLUSTERED INDEX IX_Invoice_CustID
ON Invoice(CustID);

DROP INDEX IX_Invoice_CustID ON Invoice

exec usp_FetchInvoices @SearchTerm = 'Winnie'

-- 8.
CREATE NONCLUSTERED INDEX IX_Order_StaffID
ON [Order](StaffID)
WHERE StaffID IS NOT NULL;

DROP INDEX IX_Order_StaffID ON [Order]

exec usp_FetchOrders @OrderStatus = 'COMPLETED'
-- cooked