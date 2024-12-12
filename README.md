API for the sushi restaurant management and sale website project for advanced database course using spring boot
# To-do list
\#bietthehocDS

## Report
1. List of added derived attribute. (Priority)
2. Denormalization (?)
3. List all tables with primary key, foreign key, attribute data type (?)
4. Physical design diagram. 
5. Transaction analysis
6. Index
7. Partition (?)
8. Experiment: Compare non-index and index (?)

## Database script
Priority

## Stored Procedures to Implement

### GET
P/s: 1 stored procedure -> 1 table
1. **Fetch Items**
- Fetch items with pagination. 
- Search by name, ItemID. (?)
- Filter by BranchID, category. 
- Sort by ID, price. 

2. **Fetch Staffs**
- Fetch staffs with pagination. 
- Search by StaffName and StaffID. 
- Filter by BranchID, Deparment.

3. **Fetch Reservation**
- Fetch reservations with pagination. 
- JOIN with Customer table to get CustName, CustPhoneNumber, CustEmail. 
- Search by ReservationID. 
- Filter by BranchID. 
- Sort by RsDatetime.

4. **Fetch Orders**
- Fetch orders with pagination. 
- JOIN with Customer table to get CustName, CustPhoneNumber, CustEmail. 
- Search by OrderID. Filter by BranchID, OrderType (Dine-In and Delivery) and OrderStatus (e.g., Preparing, Out for Delivery, Delivered, Completed, Cancelled). 
- Sort by OrderDateTime, estimated total price (Someone got a better name?) . 

5. **Fetch Invoices**
- Fetch invoices with pagination. 
- JOIN with Customer table to get CustName, CustPhoneNumber, CustEmail. 
- Search by InvoiceID. 
- Filter by BranchID. 
- Sort by total price.

6. **Fetch Customers**
- Fetch customers with pagination. 
- Search by Name or CustomerID (?). 

7. **Fetch Coupons**
- Fetch coupons with pagination. 
- Search by ID, code. 
- Filter by MinOrderValue, type of MinMembershipRequirement, Status(available/unavailable). 
- Sort?

8. **Fetch statistics for a branch**
- (e.g., number of orders, revenue, online accesss) I have no idea 

### INSERT
9. **Add Reservation**
- Add new reservation with a list of pre-ordered items
10. **Add Delivery Order**
- Add new delivery order with a list of ordered items
11. **Add Dine-In Order**
- Add new dine-in order with a list of ordered items
12. **Issue an Invoice**
- OrderID as a parameter. Change OrderStatus to Completed
- Add new Invoice. Apply (best?) coupon. 
- Update card points (if exists)
- Mark table as "free".
13. **Add new staff** (?)

### UPDATE
14. **Transfer staff**
- Update BranchID, Department.
- Add work history.
15. **Update Dine-In Order**
- Update :)
16. **Update Delivery Order**
- Update :)

## Generate Data
Look at the skill, look at move... Faker, WHAT WAS THAT?

To be continued...
