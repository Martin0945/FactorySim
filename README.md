# Factory Simulation

A factory simulation application developed as part of a university team project. The system models manufacturing processes, production lines, machine operations, resource management and factory maintenance.

The factory simulates the production of electric scooters and mobile phones across two production lines.

## Features

- **Production Simulation** – Simulate manufacturing processes across two production lines.
- **Resource Management** – Manage raw materials, intermediate products and production resources.
- **Machine Simulation** – Model machines and robots with different operating characteristics and resource consumption.
- **Maintenance System** – Track equipment wear and manage repair operations.
- **Factory Management** – Simulate factory supervision and inspection through director and inspector roles.
- **Cost Tracking** – Account for material, energy, oil and workforce costs.
- **Reporting** – Generate reports about factory operations and performance.

## Design Patterns

The project demonstrates several object-oriented design patterns:

- **Builder** – `SimulationBuilder` for constructing the simulation.
- **Factory** – Factory classes for creating workers, machines and robots.
- **State** – Represent different machine states, such as idle, working and broken.
- **Strategy** – Support different reporting strategies.
- **Iterator** – Manage repair operations through `RepairManager`.
- **Visitor** – Implement factory inspection using `Director` and `Inspector`.
- **Observer** – Handle simulation events through an event bus.

## Tech Stack

- Java
- Spring Boot
- Maven
- Lombok
- SnakeYAML

## Project Structure

The project is organized into four Maven modules:

- `factory-sim-core` – Core simulation logic, domain models, production processes and maintenance.
- `factory-sim-api` – API layer providing access to the simulation.
- `factory-sim-app` – Main application and Spring Boot entry point.
- `factory-sim-demo-config` – Demo configuration and YAML support.

## Getting Started

### Prerequisites

- JDK 23
- Maven
- IntelliJ IDEA (recommended)

### Installation and Running

1. Clone the repository:
   ```bash
   git clone https://github.com/Martin0945/FactorySim.git
   ```
2. Open the project in IntelliJ IDEA.
3. Import the project as a Maven project.
4. Build the project from the root directory:
   ```bash
   mvn clean install
   ```
5. Run `FactorySimApplication` from the `factory-sim-app` module.
6. Open the application in your browser:

   **http://localhost:8080**

## Team

Developed as a university team project in collaboration with:

- `macham29`
- `gruncmax`
