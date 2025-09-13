# Airline Project

This project simulates an airline management system, handling airports, flights, weather conditions, and mission planning.

## Structure

- `cases/`
  - `airports/`: CSV files with airport data.
  - `directions/`: CSV files with flight direction data.
  - `missions/`: Input files for different mission scenarios.
  - `outputs/`: Expected output files for each mission and task.
  - `times/`: Files with timing information for each scenario.
  - `weather.csv`: Weather data for scenarios.
- `src/`
  - `Airport.java`: Airport class implementation.
  - `LoungeAviation.java`: Lounge and aviation logic.
  - `Main.java`: Main entry point for running the project.

## How to Run

1. Compile the Java files in the `src/` directory.
2. Run `Main.java` with the appropriate input files from the `cases/missions/` directory.
3. Compare your output with the files in `cases/outputs/`.

## Notes

- Each scenario is organized by region (AS, EU, INTER, TR).
- Outputs are provided for two tasks per scenario.