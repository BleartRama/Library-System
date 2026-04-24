# Library System

A console-based library management system built in Java with MySQL.

## Tech Stack
- Java
- MySQL
- JDBC

## Architecture
Three-layer architecture: Controller → Service → Repository

## Setup
1. Clone the repository
2. Create a MySQL database called `bibliotek`
3. Copy `config.properties.example` to `config.properties`
4. Update `config.properties` with your database credentials
5. Run `Main.java`

## Login
| Role | Username/Email | Password |
|---|---|---|
| Librarian | `admin` | `admin123` |
| Member | email from database | password from database |

## Features

### Librarian
**Books**
- View all books
- View book details (author + categories)
- View books by category
- Add a new book (with category)
- Delete a book
- Add an author

**Members**
- View all members
- View member profile
- Create a new member

**Loans**
- View all active loans with OVERDUE status
- View loans by member
- Register a book as borrowed
- Register a book as returned

### Member
**Books**
- View all books
- View book details
- View books by category

**Loans**
- View my active loans
- Borrow a book
- Return a book

## Business Rules
- Suspended members cannot borrow books
- Books with 0 available copies cannot be borrowed
- Available copies automatically decrease when borrowed and increase when returned
- Loans past their due date are marked as OVERDUE

## Notes
- Passwords are stored in plaintext — in a real application BCrypt would be used
- Admin account is hardcoded — in a real application librarians would be stored in the database