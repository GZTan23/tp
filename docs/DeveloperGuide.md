---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

- JavaFX Tutorial adapted from [Oracle JavaFX documentation](https://openjfx.io/)
- PlantUML diagrams adapted from [se-edu PlantUML Guide](https://se-education.org/guides/tutorials/plantUml.html)
- Command parsing approach adapted from [AddressBook-Level3](https://github.com/se-edu/addressbook-level3)

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the [_Setting up and Getting Started Guide_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete_student 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="976" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `ListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Student` object and `Lesson` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete_student 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete_student 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteStudentCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteStudentCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteStudentCommandParser`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a student).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddStudentCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddStudentCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddStudentCommandParser`, `DeleteStudentCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/BetterModelClassDiagram-Class_Diagram__Student_Management_Structure.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Student` objects (which are contained in a `UniqueStudentList` object).
* stores the currently 'selected' `Student` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Student>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Student` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Student` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current TutorTrack state in its history.
* `VersionedAddressBook#undo()` — Restores the previous TutorTrack state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone TutorTrack state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial TutorTrack state, and the `currentStatePointer` pointing to that single TutorTrack state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete_student 5` command to delete the 5th student in the TutorTrack. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the TutorTrack after the `delete_student 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted TutorTrack state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add_student n/David …​` to add a new student. The `add_student` command also calls `Model#commitAddressBook()`, causing another modified TutorTrack state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the TutorTrack state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the student was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous TutorTrack state, and restores the TutorTrack to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the TutorTrack to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest TutorTrack state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the TutorTrack, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all TutorTrack states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add_student n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire TutorTrack.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete_student`, just save the student being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* is a freelance tutor, who teaches small groups or individual students
* has a need to manage a significant number of students, with various lessons and assignments
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: Quickly organise student contact details, track lesson schedules, log student progress all in one app


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​         | I want to …​                                                   | So that I can…​                                                                           |
|----------|-----------------|----------------------------------------------------------------|-------------------------------------------------------------------------------------------|
| `* * *`  | tutor           | add a new student                                              | keep track of their details                                                               |
| `* * *`  | tutor           | add lessons for each student                                   | make a lesson plan suited for my students                                                 |
| `* * *`  | tutor           | view lessons for each student                                  | have an overview of the students' learning curriculum                                     |
| `* * *`  | tutor           | assign tasks to students                                       | track student's workload and assignments                                                  |
| `* * *`  | tutor           | view list of all students                                      | view all students that I am teaching                                                      |
| `* * *`  | tutor           | delete a student                                               | remove students that I am no longer tutoring                                              |
| `* * *`  | tutor           | track completion status of assignments                         | know if my students have completed them and their past performances                       |
| `* * *`  | long-term user  | access historical logs and previous versions of student records | track and recover information when needed                                                 |
| `* *`    | tutor           | reschedule lessons                                             | make changes to lesson plans to better fit mine or my student's schedule                  |
| `* *`    | tutor           | mark lessons as complete                                       | review session history and track my students' lesson progress                             |
| `* *`    | tutor           | set personalized reminders for students                        | address individual needs effectively                                                      |
| `* *`    | tutor           | set reminders for my own tasks                                 | keep up with what I need to do                                                            |
| `* *`    | user            | add custom tags to contacts                                    | organise my contacts better                                                               |
| `* *`    | tutor           | filter students by status (e.g. active or inactive)            | manage my student's long-term engagements                                                 |
| `* *`    | tutor           | filter students by keywords and tags                           | easily find a student                                                                     |
| `* *`    | tutor           | search for a student by name                                   | quickly find their records                                                                |
| `* *`    | tutor           | update student details                                         | keep their information accurate                                                           |
| `* *`    | new user        | view help documentation                                        | understand how to interact with the application effectively                               |
| `* *`    | long-time user  | add shortcuts to commands                                      | studentalize my use of the app                                                             |
| `*`      | tutor           | export schedules to my personal calendar                       | manage the tutoring schedule with my other commitments                                    |
| `*`      | new user        | import data from a spreadsheet                                 | start to keep track of my students                                                        |
| `*`      | first time user | use commands with contextual help                              | learn proper command syntax and options without having to refer to external documentation |
| `*`      | tutor           | get a timeline overview of all events within a period of time  | view the overall structure of the schedule for said period of time                        |

### Use cases

(For all use cases below, the **System** is the `AddressBook` and the **Actor** is the `user`, unless specified otherwise)

#### Use Case 1: Add a New Student

**System**: TutorTrack

**Actor**: Tutor

**Use Case**: UC01 - Add a New Student

**MSS**:

1. Tutor chooses to add a new student.
2. TutorTrack prompts for student details (name, contact, subjects).
3. Tutor enters the required details.
4. TutorTrack saves the student details and confirms the addition.

   Use case ends.


**Extensions**:

- **3a**: TutorTrack detects a duplicate student name.
    - 3a1: TutorTrack warns the tutor about the duplicate.
    - 3a2: Tutor confirms whether to proceed or cancel.
    - 3a3: If tutor confirms, TutorTrack saves the student details.
    - 3a4: If tutor cancels, TutorTrack discards the input.
    - Use case resumes from step 4 or ends.

#### Use Case 2: View Lesson History for a Student

**System**: TutorTrack

**Actor**: Tutor

**Use Case**: UC02 - View Lesson History

**MSS**:

1. Tutor chooses to view lesson history.
2. TutorTrack prompts for the student’s name.
3. Tutor enters the student’s name.
4. TutorTrack displays the lesson history for the student.

   Use case ends.


**Extensions**:

- **3a**: TutorTrack cannot find the student.
    - 3a1: TutorTrack informs the tutor that the student does not exist.
    - 3a2: Tutor chooses to add the student or retry the search.
    - Use case resumes from step 2 or ends.
- **4a**: Tutor chooses to filter the lesson history by date range or subject.
    - 4a1: TutorTrack prompts for the filter criteria.
    - 4a2: Tutor enters the filter criteria.
    - 4a3: TutorTrack displays the filtered lesson history.
    - Use case ends.

#### Use Case 3: Assign Homework to a Student

**System**: TutorTrack

**Actor**: Tutor

**Use Case**: UC03 - Assign Homework

**MSS**:

1. Tutor chooses to assign homework.
2. TutorTrack prompts for the student’s name.
3. Tutor enters the student’s name.
4. TutorTrack prompts for homework details (task name, due date, description).
5. Tutor enters the homework details.
6. TutorTrack saves the homework assignment and confirms the assignment.

   Use case ends.


**Extensions**:

- **3a**: TutorTrack cannot find the student.
    - 3a1: TutorTrack informs the tutor that the student does not exist.
    - 3a2: Tutor chooses to add the student or retry the search.
    - Use case resumes from step 2 or ends.
- **5a**: Tutor enters an invalid due date (e.g., past date).
    - 5a1: TutorTrack warns the tutor about the invalid date.
    - 5a2: Tutor enters a valid date.
    - Use case resumes from step 6.

#### Use Case 4: Update Student Progress After a Lesson

**System**: TutorTrack

**Actor**: Tutor

**Use Case**: UC04 - Update Student Progress

**MSS**:

1. Tutor chooses to update student progress.
2. TutorTrack prompts for the student’s name.
3. Tutor enters the student’s name.
4. TutorTrack prompts for progress details (e.g., understanding level, quiz scores, notes).
5. Tutor enters the progress details.
6. TutorTrack saves the progress update and confirms the update.

   Use case ends.


**Extensions**:

- **3a**: TutorTrack cannot find the student.
    - 3a1: TutorTrack informs the tutor that the student does not exist.
    - 3a2: Tutor chooses to add the student or retry the search.
    - Use case resumes from step 2 or ends.
- **5a**: Tutor enters invalid data (e.g., quiz score out of range).
    - 5a1: TutorTrack warns the tutor about the invalid data.
    - 5a2: Tutor enters valid data.
    - Use case resumes from step 6.

#### Use Case 5: Delete a Student

**System**: TutorTrack

**Actor**: Tutor

**Use Case**: UC05 - Delete a Student

**MSS**:

1. Tutor chooses to delete a student.
2. TutorTrack prompts for the student’s name.
3. Tutor enters the student’s name.
4. TutorTrack confirms the deletion.
5. Tutor confirms the deletion.
6. TutorTrack deletes the student and confirms the deletion.

   Use case ends.

**Extensions**:

- **3a**: TutorTrack cannot find the student.
    - 3a1: TutorTrack informs the tutor that the student does not exist.
    - 3a2: Tutor chooses to retry the search or cancel.
    - Use case resumes from step 2 or ends.
- **5a**: Tutor cancels the deletion.
    - 5a1: TutorTrack discards the deletion request.
    - Use case ends.

#### Use Case 6: Track Assignment Completion

**System**: TutorTrack

**Actor**: Tutor

**Use Case**: UC06 - Track Assignment Completion

**MSS**:

1. Tutor chooses to track assignment completion.
2. TutorTrack prompts for the student’s name.
3. Tutor enters the student’s name.
4. TutorTrack displays the list of assignments for the student.
5. Tutor marks an assignment as completed.
6. TutorTrack updates the assignment status and confirms the update.

   Use case ends.


**Extensions**:

- **3a**: TutorTrack cannot find the student.
    - 3a1: TutorTrack informs the tutor that the student does not exist.
    - 3a2: Tutor chooses to add the student or retry the search.
    - Use case resumes from step 2 or ends.
- **5a**: Tutor marks an assignment as incomplete.
    - 5a1: TutorTrack updates the assignment status and confirms the update.
    - Use case ends.

#### Use Case 7: Access Historical Logs

**System**: TutorTrack

**Actor**: Tutor

**Use Case**: UC07 - Access Historical Logs

**MSS**:

1. Tutor chooses to access historical logs.
2. TutorTrack prompts for the student’s name.
3. Tutor enters the student’s name.
4. TutorTrack displays the historical logs for the student.

   Use case ends.


**Extensions**:

- **3a**: TutorTrack cannot find the student.
    - 3a1: TutorTrack informs the tutor that the student does not exist.
    - 3a2: Tutor chooses to add the student or retry the search.
    - Use case resumes from step 2 or ends.
- **4a**: Tutor chooses to filter logs by date range or type (e.g., lessons, assignments).
    - 4a1: TutorTrack prompts for the filter criteria.
    - 4a2: Tutor enters the filter criteria.
    - 4a3: TutorTrack displays the filtered logs.
    - Use case ends.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 students without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  Any command should respond in 10 seconds or less for up to 100 students.
5.  System should be usable by a user who is comfortable with typing and using a CLI.
6.  System should be able to store data for at least 1 year without data loss.
7.  Project is expected to adhere to the [Java Code Quality Guide](https://se-education.org/guides/contributing/javaCodeQualityGuide.html).
8.  Project is expected to adhere to a schedule that delivers a feature set every week.
9.  This project is not expected to connect to the internet or any external services (email, cloud storage, telegram, etc).

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, macOS
* **Tutor**: A person who does free-lance tutoring
* **Student**: A person that is being or has been tutored by the current user of the application
* **Spreadsheet**: An Excel spreadsheet
* **Assignment**: A homework assignment or task that has been given by the tutor to the student
* **Historical logs**: The records of students' details and the changes that have been made during the lifetime use of the app

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a student

1. Deleting a student while all students are being shown

   1. Prerequisites: List all students using the `list_students` command. Multiple students in the list.

   1. Test case: `delete_student 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete_student 0`<br>
      Expected: No student is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete_student`, `delete_student x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_

## Appendix: Effort

**Team size**: 5

**Difficulty level**: Moderate to High

**Challenges faced**:

1. **Complexity of Features**: Implementing features such as lessons and assignments required a deep understanding of the existing codebase and careful planning to ensure seamless integration.
2. **Data Management**: Handling multiple entity types (students, lessons, assignments) added complexity to the data management and storage components.
3. **User Interface**: Ensuring the CLI-based interface remains user-friendly while accommodating new features was challenging.
4. **Testing**: Writing comprehensive tests for new features required significant effort.

**Effort required**:

- **Planning and Design**: Approximately 20 hours were spent on planning and designing the new features and their integration with the existing system.
- **Implementation**: Around 80 hours were dedicated to coding, debugging, and refining the new features.
- **Testing**: About 30 hours were spent on writing and executing test cases to ensure the reliability of the new features.
- **Documentation**: Approximately 10 hours were used to update the Developer Guide and User Guide to reflect the new features and changes.

**Achievements**:

- Successfully implemented the lessons and assignments feature, allowing users to add lessons and assignments to students with ease.
- Enhanced data management capabilities to handle multiple entity types efficiently.
- Improved the user interface to provide better feedback and usability.
- Maintained high code quality and adherence to coding standards throughout the project.

**Reuse**:

- The lessons/assignments feature was inspired by similar implementations in other projects, but our implementation was tailored to fit the specific needs of TutorTrack.
- Some utility functions and classes were reused from the AddressBook-Level3 project, with minimal modifications to suit our requirements.

## Planned Enhancements

1. **Enhanced Error Messages**: Improve error messages to be more specific and actionable. For example, instead of showing "Operation failed!", the message could indicate the exact reason for the failure, such as "The student 'John Doe' could not be added because the name already exists."
2. **Data Validation**: Implement more robust data validation to prevent invalid inputs from being processed. For instance, validate email formats, phone numbers, and date formats before saving them.
3. **Batch Operations**: Add support for batch operations, such as adding multiple students or assignments at once, to improve efficiency for users managing large datasets.
4. **Improved Search Functionality**: Enhance the search feature to support more complex queries, such as searching by multiple criteria (e.g., name, subject, and status) simultaneously.
5. **Export Functionality**: Allow users to export data to external formats (e.g., CSV, Excel) for easier sharing and analysis outside the application.
6. **Customizable Reminders**: Enable users to set customizable reminders for lessons and assignments, with options for recurring reminders and notifications.
7. **User Profiles**: Implement user profiles to allow multiple tutors to use the application with personalized settings and data separation.
8. **Performance Optimization**: Optimize the application's performance to handle larger datasets more efficiently, ensuring smooth operation even with thousands of entries.
9. **Enhanced Reporting**: Add reporting features to generate summaries and insights, such as student progress reports, lesson attendance, and assignment completion rates.
10. **Integration with Calendars**: Allow users to integrate their lesson schedules with external calendar applications (e.g., Google Calendar) for better schedule management.
