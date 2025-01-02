--1. Non-clustered Index cho MenuItem(ItemName) 
DROP INDEX IX_MenuItem_ItemName ON MenuItem;
CREATE NONCLUSTERED INDEX IX_MenuItem_ItemName
ON MenuItem(ItemName);
exec usp_FetchItems @SearchTerm = 'Bánh'

--2. Non-clustered Index cho Customer(CustName)
DROP INDEX IX_Customer_CustName ON Customer;
CREATE NONCLUSTERED INDEX IX_Customer_CustName
ON Customer(CustName);
exec usp_FetchCustomers @SearchTerm = 'Winne'

--3. Non-clustered Index cho Reservation(CustomerID)
DROP INDEX IX_Reservation_CustID ON Reservation;
CREATE NONCLUSTERED INDEX IX_Reservation_CustID
ON Reservation(CustID);
exec usp_FetchReservations @CustID = 547;

--4. Non-clustered Index cho Order(CustID)
DROP INDEX IX_Order_CustID ON [Order];
CREATE NONCLUSTERED INDEX IX_Order_CustID
ON [Order](CustID);
exec usp_FetchOrders @CustID = 1;

--5. Non-clustered Index cho Invoice(CustID)
CREATE NONCLUSTERED INDEX IX_Invoice_CustID
ON Invoice(CustID);
exec usp_FetchInvoices @CustID = 547;