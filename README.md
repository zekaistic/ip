# Chungus User Guide

Welcome to **Chungus** - your friendly task management chatbot! 🐰

Chungus helps you stay organized by managing your tasks, deadlines, and events. You can interact with Chungus through either a graphical user interface (GUI) or command-line interface (CLI).

## Quick Start

### GUI Mode
Run the application and you'll see a chat interface where you can type commands and see Chungus's responses.

### Command Line Mode
Run the application from terminal to interact with Chungus through text commands.

## Available Commands

### 📋 Viewing Tasks

#### `list`
Shows all your current tasks with their status and details.

**Example:**
```
list
```

**Expected output:**
```
Here are the tasks in your list:
1. [T][ ] read book
2. [D][ ] return book (by: Dec 12 2024)
3. [E][ ] project meeting (from: Jan 1 2025 to: Jan 2 2025)
```

#### `find <keyword>`
Searches for tasks containing the specified keyword.

**Example:**
```
find book
```

**Expected output:**
```
Here are the matching tasks in your list:
1. [T][ ] read book
2. [D][ ] return book (by: Dec 12 2024)
```

### ✅ Managing Tasks

#### `mark <number>`
Marks a task as completed.

**Example:**
```
mark 1
```

**Expected output:**
```
Nice! I've marked this task as done:
  [T][X] read book
```

#### `unmark <number>`
Marks a task as not completed.

**Example:**
```
unmark 1
```

**Expected output:**
```
OK, I've marked this task as not done yet:
  [T][ ] read book
```

#### `delete <number>`
Removes a task from your list.

**Example:**
```
delete 2
```

**Expected output:**
```
Noted. I've removed this task:
  [D][ ] return book (by: Dec 12 2024)
Now you have 2 tasks in the list.
```

#### `priority <number> <level>`
Sets the priority level for a task (high, medium, low).

**Example:**
```
priority 1 high
```

**Expected output:**
```
Priority set to high for task 1: read book
```

### ➕ Adding Tasks

#### `todo <description>`
Adds a simple todo task.

**Example:**
```
todo read book
```

**Expected output:**
```
Got it. I've added this task:
  [T][ ] read book
Now you have 1 task in the list.
```

#### `deadline <description> /by <date>`
Adds a task with a deadline.

**Date formats supported:**
- `yyyy-MM-dd` (e.g., 2024-12-31)
- `d/M/yyyy` (e.g., 31/12/2024)
- `d-M-yyyy` (e.g., 31-12-2024)

**Example:**
```
deadline return book /by 2024-12-31
```

**Expected output:**
```
Got it. I've added this task:
  [D][ ] return book (by: Dec 31 2024)
Now you have 2 tasks in the list.
```

#### `event <description> /from <start> /to <end>`
Adds an event with start and end dates.

**Date formats supported:**
- `yyyy-MM-dd` (e.g., 2024-12-31)
- `d/M/yyyy` (e.g., 31/12/2024)
- `d-M-yyyy` (e.g., 31-12-2024)

**Example:**
```
event project meeting /from 2025-01-01 /to 2025-01-02
```

**Expected output:**
```
Got it. I've added this task:
  [E][ ] project meeting (from: Jan 1 2025 to: Jan 2 2025)
Now you have 3 tasks in the list.
```

### ℹ️ Other Commands

#### `help`
Shows all available commands and their usage.

**Example:**
```
help
```

#### `bye`
Exits the application and saves your tasks.

**Example:**
```
bye
```

**Expected output:**
```
Bye. Hope to see you again soon!
```

## Task Types

- **[T]** - Todo: Simple tasks without specific timing
- **[D]** - Deadline: Tasks with a due date
- **[E]** - Event: Tasks that span a specific time period

## Task Status

- **[ ]** - Not completed
- **[X]** - Completed

## Priority Levels

- **high** - High priority tasks
- **medium** - Medium priority tasks (default)
- **low** - Low priority tasks

## Tips

- Use task numbers from the `list` command to reference specific tasks
- Dates are flexible - use any of the supported formats
- Your tasks are automatically saved when you exit with `bye`
- Type `help` anytime to see all available commands
- The GUI provides a more visual experience with chat bubbles

## Getting Help

If you're ever unsure about how to use a command, just type `help` and Chungus will show you all available commands with examples!
