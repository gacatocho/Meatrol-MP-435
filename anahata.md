# VistaDatosME435 - Data Analysis Project

## Overview
This project is designed to read analyzer records in CSV format and generate various charts and analyses.

## Architecture
- **baseVDM**: Base module for shared functionality.
- **dataVDM**: Data module, responsible for loading and displaying CSV data.

## Key Components
### dataTopComponent
- Located in `org.gcto.dataVDM`.
- Displays a table with up to 1 million rows and 80 columns.
- Features a 32px high toolbar with a CSV loading button and a delete row button.
- Supports row deletion.
- **Column structure**: Dynamic, based on a **three-row CSV header**. 
    - Row 1: Metadata (e.g., "ProductSN : 12345"). Currently skipped.
    - Row 2: Main categories (e.g., "Voltaje(V)", "UTHD(%)").
    - Row 3: Sub-categories (e.g., "L1", "L2", "L3").
    - The system merges Rows 2 and 3 into combined headers (e.g., "Voltaje(V): L1").
    - Data types are inferred from keywords like "Date", "Time", and "%".
