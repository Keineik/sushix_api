# SushiX API

SushiX API is a robust backend solution for managing a sushi restaurant's operations, developed as part of an advanced database course. This project is built using **Spring Boot** for the backend and **TSQL** for database management, offering functionalities to handle reservations, orders, staff, inventory, and more.

---

## Key Features

- **Comprehensive Restaurant Management**: Manage reservations, orders (dine-in and delivery), staff transfers, invoices, and branch statistics.
- **RESTful Endpoints**: Provides a wide range of endpoints for CRUD operations, allowing seamless interaction with the system.
- **Efficient Database Design**: Implements advanced database concepts such as indexing, partitioning, and transaction optimization to ensure high performance.
- **Pagination and Filtering**: Fetch data with built-in pagination, search, and filtering capabilities for scalability and user convenience.
- **Dynamic Statistics**: Generate branch-specific statistics, including revenue, order counts, and customer insights.

---

## Technologies Used

- **Backend Framework**: Java with Spring Boot
- **Database**: Microsoft SQL Server with TSQL for writing stored procedures and database scripts
- **Design Principles**: RESTful API design, MVC architecture, and advanced database normalization/denormalization techniques

---

## Installation and Setup

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Keineik/sushix_api.git
   cd sushix_api
   ```

2. **Set Up the Database**:
   - Create a Microsoft SQL Server database.
   - Run the provided TSQL scripts in the `database` directory to set up tables, stored procedures, and initial data.

3. **Configure Application Properties**:
   - Update the `application.properties` file in the `src/main/resources` directory with your database connection details:
     ```properties
     spring.datasource.url=jdbc:sqlserver://<your-database-url>:<port>;databaseName=<database-name>
     spring.datasource.username=<your-database-username>
     spring.datasource.password=<your-database-password>
     ```

4. **Run the Application**:
   - Use Maven to build and start the server:
     ```bash
     mvn spring-boot:run
     ```

5. **Access the API**:
   - The API will be available at `http://localhost:8080`.

---

## API Endpoints Overview

### Reservations
- Create, update, and fetch reservations.
- Includes customer details and pre-ordered items.

### Orders
- Manage dine-in and delivery orders.
- Support for filters such as order type, status, and branch.

### Staff
- Add and transfer staff between branches.
- Maintain a work history for staff members.

### Customers
- Fetch customer details with search and pagination.

### Coupons
- Manage and fetch available discount coupons.

### Branch Statistics
- Generate insights for a branch, including revenue and order metrics.

---

## Database Features

- **Physical Design**: Includes optimized table schemas, primary keys, and foreign keys.
- **Stored Procedures**: Implements stored procedures for fetching, inserting, and updating data.
- **Performance Optimization**: Utilizes indexing, partitioning, and transaction management for efficient operations.

---

## Future Enhancements

- Add automated testing for all endpoints.
- Extend API documentation with examples for each endpoint.
- Implement authentication and authorization for secure access.
- Optimize database queries for even better performance.

---

## Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes. Ensure that your code adheres to the project's coding standards and is thoroughly tested.

---

## License

This project is licensed under the [MIT License](LICENSE).
