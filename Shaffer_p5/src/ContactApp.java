import java.util.Scanner;
import java.io.File;

public class ContactApp {
    private static final Scanner scan = new Scanner(System.in);

    public static void main(String [] args) {
        mainContactMenu();
    }

    public static void mainContactMenu() {
        while(true) {
            System.out.print("\nMain Menu\n");
            System.out.print("---------\n\n");
            System.out.println("1) create a new list");
            System.out.println("2) load an existing list");
            System.out.println("3) quit");

            String choice = scan.nextLine();

            switch(choice) {
                case "1" -> {
                    System.out.println("new contact list has been created\n");
                    listOperationMenu(new ContactList());
                }
                case "2" -> {
                    ContactList retrieved = loadList();
                    if (retrieved != null) {
                        listOperationMenu(retrieved);
                    }
                }
                case "3" -> {
                    return;
                }
                default -> System.out.println("\nNot a valid selection. Please try again.\n");
            }
        }
    }

    public static void listOperationMenu(ContactList contactList) {
        while (true) {
            System.out.print("\nList Operation Menu\n");
            System.out.print("-------------------\n\n");
            System.out.println("1) view the list");
            System.out.println("2) add an item");
            System.out.println("3) edit an item");
            System.out.println("4) remove an item");
            System.out.println("5) save the current list");
            System.out.println("6) quit to the main menu\n");

            String choice = scan.nextLine();
            switch(choice) {
                case "1" -> System.out.println(contactList.toString());
                case "2" -> addItem(contactList);
                case "3" -> editItem(contactList);
                case "4" -> removeItem(contactList);
                case "5" -> saveList(contactList);
                case "6" -> {
                    return;
                }
                default -> System.out.println("\nNot a valid selection. Please try again.\n");
            }
        }
    }

    public static ContactList loadList() {
        File saveDir = new File("SaveFiles/");
        String[] savedFileNames = saveDir.list();
        if (savedFileNames != null) {
            System.out.print("\nenter the filename to load (or 'cancel' to quit): ");
        } else {
            System.out.print("ERROR: no saved lists yet. go make some first.\n");
            return null;
        }
        ContactList newLoad = new ContactList();
        while (true) {
            String fileName = scan.nextLine();
            if(fileName.equalsIgnoreCase("cancel")) {
                return null;
            }
            else if(newLoad.loadList(fileName)) {
                return newLoad;
            }
            else {
                System.out.print("\nplease enter another filename (or 'cancel' to quit): ");
            }
        }
    }

    public static ContactItem getContactItemIdentifiersFromInput(String action) {
        System.out.printf("\nfirst name: ", action.equals("edit") ? "New " : "");
        String firstName = scan.nextLine();
        // Can have blank fields, but fails if ALL fields are blank
        System.out.printf("\nlast name: ", action.equals("edit") ? "New " : "");
        String lastName = scan.nextLine();

        System.out.printf("\nphone number (xxx-xxx-xxxx): ", action.equals("edit") ? "New " : "");
        String phoneNumber = scan.nextLine();

        System.out.printf("\nemail address (x@y.z): ", action.equals("edit") ? "New " : "");
        String emailAddress = scan.nextLine();

        ContactItem item = new ContactItem(firstName, lastName, phoneNumber, emailAddress);
        while (item.validContactItem()) {
            System.out.print("\nWARNING: all fields left blank. Please try again\n");
            break;
        }
        return item;
    }

    public static ContactItem getContactItemFromInput(ContactList contactList) {
        String input = scan.nextLine();
        try {
            int contactNumber = Integer.parseInt(input);
            ContactItem item = contactList.getContact(contactNumber);
            if (item == null) {
                System.out.printf("WARNING: cannot find by index, trying by title '%s'\n", input);
                item = contactList.getContact(input);
                if (item == null) {
                    System.out.print("ERROR: no contact with that title exists.");
                }
            }
            return item;
        } catch (NumberFormatException error) {
            System.out.print("searching for contact with that title...");
            ContactItem item = contactList.getContact(input);
            if (item == null) {
                System.out.print("ERROR: no contact with that title exists.");
            }
            return item;
        }
    }

    public static void addItem(ContactList contactList) {
        ContactItem newItem = getContactItemIdentifiersFromInput("add");
        contactList.addContact(newItem);
        System.out.println("SUCCESS: added contact to list.");
    }

    public static void editItem(ContactList contactList) {
        System.out.print("\n" + contactList.toString() + "\n");
        System.out.print("\ncontact to be edited: ");
        ContactItem oldContact = getContactItemFromInput(contactList);
        if (oldContact == null) {
            System.out.print("\nWARNING: no contact was found by that name or index. no changes made to list.");
        } else {
            ContactItem editedContact = getContactItemIdentifiersFromInput("edit");
            oldContact.setFirstName(editedContact.getFirstName());
            oldContact.setLastName(editedContact.getLastName());
            oldContact.setPhoneNumber(editedContact.getPhoneNumber());
            oldContact.setEmailAddress(editedContact.getEmailAddress());
            System.out.print("SUCCESS: edited contact.");
        }
    }

    public static void removeItem(ContactList contactList) {
        System.out.print("\n" + contactList.toString() + "\n");
        System.out.print("\ncontact to be removed: ");
        ContactItem contact = getContactItemFromInput(contactList);
        if (contact == null) {
            System.out.print("\nWARNING: no contact found by that name or index. no changes made to list.");
        } else {
            boolean success = contactList.removeContact(contact);
            if (success) {
                System.out.print("\nSUCCESS: removed contact from list.\n");
            } else {
                System.out.print("\nERROR: cannot remove contact from list.\n");
            }
        }
    }

    public static void saveToFile(ContactList contactList) {
        System.out.print("\nenter the filename to save as: ");
        String name = scan.nextLine();
        if (!name.toLowerCase().contains(".txt"))
            name += ".txt";

        File file = new File("SaveFiles/" + name);
        if (file.exists() && !file.isDirectory()) {
            contactList.saveList(name);
        } else {
            contactList.saveList(name);
        }
    }

    public static void saveList(ContactList contactList) {
        boolean hasDefault = contactList.hasDefaultSaveLocation();
        if (hasDefault) {
            System.out.print("\nthere is a default save location for this file. save to this location? (Y/N)\n");
            String yn = scan.nextLine();
            if (yn.equalsIgnoreCase("y") || yn.equalsIgnoreCase("yes")) {
                contactList.saveList();
            } else if (yn.equalsIgnoreCase("n") || yn.equalsIgnoreCase("no")) {
                saveToFile(contactList);
            } else {
                System.out.print("\nERROR: invalid choice. no changes made.\n");
            }
        } else {
            saveToFile(contactList);
        }
    }
}

