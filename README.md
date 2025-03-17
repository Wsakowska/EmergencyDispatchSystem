# EmergencyDispatchSystem

## Overview
The `EmergencyDispatchSystem` is a Java-based application designed to simulate an emergency response system. It models the process of reporting accidents, generating emergency calls, dispatching response units (police, ambulance, fire brigade), and managing incidents over a turn-based simulation. The system includes optional hospital functionality for handling patients requiring hospitalization.

## Features
- **Accident Reporting**: Tracks incidents with details like location, type, and number of injured persons.
- **Emergency Call Management**: Generates and prioritizes calls, assigning response units based on proximity and availability.
- **Dispatcher Logic**: Coordinates active incidents and dispatches units efficiently.
- **Response Units**: Simulates police, ambulance, and fire brigade movements and actions.
- **Turn-Based Simulation**: Runs scenarios with random or predefined events across multiple turns.
- **Hospital Integration (Optional)**: Manages patient assignments to hospitals with limited bed capacity.

## Project Structure
- **Simulation**: Implements a turn-based system with at least 5 event types requiring various unit combinations.

## Requirements
- Java 8 or higher (e.g., OpenJDK or Oracle JDK).
- A Java IDE (e.g., IntelliJ IDEA, Eclipse) or command-line tools (e.g., `javac`, `java`).

## Installation
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Wsakowska/EmergencyDispatchSystem.git
   cd EmergencyDispatchSystem
   ```

2. **Compile the Code**:
   ```bash
   javac src/main/java/system/*.java src/main/java/hospital/*.java
   ```

3. **Run the Simulation**:
   ```bash
   java -cp src/main/java system.Symulacja
   ```
   
## Example 
   ```bash
=== Tura 14 ===
Szpital Szpital B wypisał pacjenta dummyPesel82236494333750
-- Aktywne zgłoszenia --
   Zgloszenie{id=3, czas=3, lok=[5.00, 5.00], status=AKTYWNE, turPotrzebne=4, turWykonane=12, pomocy=3/2, hosp=0, odwiezionych=2}
   Zgloszenie{id=4, czas=3, lok=[8.00, 15.00], status=AKTYWNE, turPotrzebne=3, turWykonane=12, pomocy=0/1, hosp=0, odwiezionych=0}
   Zgloszenie{id=5, czas=4, lok=[2.00, 10.00], status=AKTYWNE, turPotrzebne=2, turWykonane=11, pomocy=1/3, hosp=1, odwiezionych=1}
   Zgloszenie{id=6, czas=5, lok=[12.00, 3.00], status=AKTYWNE, turPotrzebne=4, turWykonane=10, pomocy=4/2, hosp=0, odwiezionych=3}
   Zgloszenie{id=7, czas=8, lok=[6.00, 6.00], status=AKTYWNE, turPotrzebne=3, turWykonane=7, pomocy=1/3, hosp=1, odwiezionych=1}
-- Zakończone zgłoszenia --
   Zgloszenie{id=1, czas=0, lok=[3.00, 3.00], status=ZAKONCZONE, turPotrzebne=2, turWykonane=4, pomocy=2/0, hosp=0, odwiezionych=1}
   Zgloszenie{id=2, czas=2, lok=[10.00, 9.00], status=ZAKONCZONE, turPotrzebne=3, turWykonane=3, pomocy=3/0, hosp=0, odwiezionych=1}
-- Jednostki --
   Policja{id=POL-1, status=NA_MIEJSCU, lok=[5.00, 5.00], celPodrozy=[5.00, 5.00], dist=0.00}
   Policja{id=POL-2, status=DOJEZDZAM_DO_ZDARZENIA, lok=[3.00, 3.00], celPodrozy=[3.00, 3.00], dist=0.00}
   Pogotowie{id=AMB-1, status=TRANSPORT_DO_SZPITALA, lok=[6.00, 6.00], celPodrozy=[8.00, 8.00], dist=2.83}
   Pogotowie{id=AMB-2, status=DOJEZDZAM_DO_ZDARZENIA, lok=[3.00, 3.00], celPodrozy=[3.00, 3.00], dist=0.00}
   Pogotowie{id=AMB-3, status=DOSTEPNA, lok=[3.00, 7.00], celPodrozy=[3.00, 7.00], dist=0.00}
   Pogotowie{id=AMB-4, status=NA_MIEJSCU, lok=[10.00, 9.00], celPodrozy=[10.00, 9.00], dist=0.00}
   StrazPozarna{id=STR-1, status=NA_MIEJSCU, lok=[10.00, 9.00], celPodrozy=[10.00, 9.00], dist=0.00}
   StrazPozarna{id=STR-2, status=NA_MIEJSCU, lok=[5.00, 5.00], celPodrozy=[5.00, 5.00], dist=0.00}
-- Szpitale --
   Szpital Szpital A, dost. miejsc: 3/3, lok=[2.00, 2.00]
   Szpital Szpital B, dost. miejsc: 4/5, lok=[8.00, 8.00]

=== Tura 15 ===
Szpital Szpital B przyjął pacjenta dummyPesel82236499236416
Szpital Szpital B przyjął pacjenta z karetki AMB-1
-- Aktywne zgłoszenia --
   Zgloszenie{id=3, czas=3, lok=[5.00, 5.00], status=AKTYWNE, turPotrzebne=4, turWykonane=13, pomocy=3/2, hosp=0, odwiezionych=2}
   Zgloszenie{id=4, czas=3, lok=[8.00, 15.00], status=AKTYWNE, turPotrzebne=3, turWykonane=13, pomocy=0/1, hosp=0, odwiezionych=0}
   Zgloszenie{id=5, czas=4, lok=[2.00, 10.00], status=AKTYWNE, turPotrzebne=2, turWykonane=12, pomocy=2/2, hosp=0, odwiezionych=2}
   Zgloszenie{id=6, czas=5, lok=[12.00, 3.00], status=AKTYWNE, turPotrzebne=4, turWykonane=11, pomocy=5/1, hosp=0, odwiezionych=3}
   Zgloszenie{id=7, czas=8, lok=[6.00, 6.00], status=AKTYWNE, turPotrzebne=3, turWykonane=8, pomocy=1/3, hosp=1, odwiezionych=1}
-- Zakończone zgłoszenia --
   Zgloszenie{id=1, czas=0, lok=[3.00, 3.00], status=ZAKONCZONE, turPotrzebne=2, turWykonane=4, pomocy=2/0, hosp=0, odwiezionych=1}
   Zgloszenie{id=2, czas=2, lok=[10.00, 9.00], status=ZAKONCZONE, turPotrzebne=3, turWykonane=3, pomocy=3/0, hosp=0, odwiezionych=1}
-- Jednostki --
   Policja{id=POL-1, status=NA_MIEJSCU, lok=[5.00, 5.00], celPodrozy=[5.00, 5.00], dist=0.00}
   Policja{id=POL-2, status=DOJEZDZAM_DO_ZDARZENIA, lok=[3.00, 3.00], celPodrozy=[3.00, 3.00], dist=0.00}
   Pogotowie{id=AMB-1, status=POWROT_DO_BAZY, lok=[8.00, 8.00], celPodrozy=[0.00, 5.00], dist=8.54}
   Pogotowie{id=AMB-2, status=DOJEZDZAM_DO_ZDARZENIA, lok=[3.00, 3.00], celPodrozy=[3.00, 3.00], dist=0.00}
   Pogotowie{id=AMB-3, status=TRANSPORT_DO_SZPITALA, lok=[2.00, 10.00], celPodrozy=[8.00, 8.00], dist=6.32}
   Pogotowie{id=AMB-4, status=NA_MIEJSCU, lok=[10.00, 9.00], celPodrozy=[10.00, 9.00], dist=0.00}
   StrazPozarna{id=STR-1, status=NA_MIEJSCU, lok=[10.00, 9.00], celPodrozy=[10.00, 9.00], dist=0.00}
   StrazPozarna{id=STR-2, status=NA_MIEJSCU, lok=[5.00, 5.00], celPodrozy=[5.00, 5.00], dist=0.00}
-- Szpitale --
   Szpital Szpital A, dost. miejsc: 3/3, lok=[2.00, 2.00]
   Szpital Szpital B, dost. miejsc: 3/5, lok=[8.00, 8.00]
   ```

## Usage
- Launch the simulation via the `Symulacja` class.
- The system will generate random accidents or use a predefined event list.
- Observe turn-by-turn output detailing dispatcher actions and unit movements.
- Modify simulation parameters (e.g., event types, unit distances) in the source code as needed.

## Simulation Details
- **Turn Mechanics**: New accidents occur, units move, and actions are logged per turn.
- **Event Types**: Includes scenarios like car crashes (police + ambulance), fires (fire brigade + ambulance), and mass casualty events (all units).
- **Hospital Mode**: Optionally enabled with two hospitals managing patient intake.
