import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TelephoneBook {
    private List<Contact> contactList;
    private BufferedReader reader;

    public TelephoneBook() {
        contactList = new ArrayList<>();
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    private void printBasicMenu() {
        System.out.println("\n<----------------- Action Panel ----------------->");
        System.out.println("|   <1> View contact list                         |");
        System.out.println("|   <2> Add a new contact                         |");
        System.out.println("|   <3> Update an existing contact                |");
        System.out.println("|   <4> Delete Contact                            |");
        System.out.println("|   <5> View contacts starting with ...           |");
        System.out.println("|   <6> View contacts older / younger ... years   |");
        System.out.println("|   <7> Export contacts to file                   |");
        System.out.println("|   <8> Export one contact to file                |");
        System.out.println("|   <9> Import contacts to file                   |");
        System.out.println("|   <10> Import one contact to file               |");
        System.out.println("|   <0> Exit                                      |");
        System.out.println("<------------------------------------------------->\n");
    }

    private void printChangeMenu() {
        System.out.println("\n<------------------ Action Panel ------------------>");
        System.out.println("|    <1> Change contact surname                      |");
        System.out.println("|    <2> Change contact name                         |");
        System.out.println("|    <3> Change contact number                       |");
        System.out.println("|    <4> Change contact's birth date                 |");
        System.out.println("|    <0> Return to the main menu                     |");
        System.out.println("<--------------------------------------------------->\n");
    }

    private void printAgeMenu() {
        System.out.println("\n<------------------ Action Panel ------------------>");
        System.out.println("|    <1> Older than a certain age                   |");
        System.out.println("|    <2> Younger than a certain age                 |");
        System.out.println("|    <3> Growers                                    |");
        System.out.println("|    <0> Return to the main menu                    |");
        System.out.println("<--------------------------------------------------->\n");
    }

    public void run() {
        int userChoise = 0;
        do {
            boolean check = false;
            while (!check) {
                try {
                    printBasicMenu();
                    System.out.print("Choose the number from the menu above: ");
                    userChoise = Integer.parseInt(reader.readLine());

                    if (userChoise < 0 || userChoise > 10) {
                        throw new IllegalArgumentException("Your number should be greater than 0 and smaller than 10");
                    }

                    check = true;
                    processBasicMenuInput(userChoise);
                } catch (NumberFormatException e) {
                    System.out.println("You should write the number!!!");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        while (userChoise != 0);
    }


    private void processBasicMenuInput(int userChoise) throws IOException {
        switch (userChoise) {
            case 1:
                seeContacts(contactList);
                break;
            case 2:
                addContact(promptContact());
                break;
            case 3:
                updateContactinfo();
                break;
            case 4:
                deleteContact();
                break;
            case 5:
                printContactsWithPrefix();
                break;
            case 6:
                printContactsUsingAgefilter();
                break;
            case 7:
                exportTo();
                break;
            case 8:
                exportOneContact();
                break;
            case 9:
                importFrom();
                break;
            case 10:
                importOneContact();
                break;
        }
    }

    private void importOneContact() throws IOException {
        System.out.println("Enter the name of the file in format *name*.txt");
        String fileName = reader.readLine().toLowerCase();
        List<Contact> myList = new ArrayList<>();
        int counter = 0;
        if (!fileName.endsWith(".txt")) {
            fileName += ".txt";
        }
        try (FileInputStream fis = new FileInputStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            String rd;
            while ((rd = reader.readLine()) != null) {
                String[] args = rd.split("\\|");
                myList.add(new Contact(args[0], args[1], args[2], new BirthDate(args[3])));
                counter++;
            }

            if (counter > 0) {
                int index = promptIndex(myList);
                contactList.add(myList.get(index));
                System.out.printf("Contact was imported from %s successfully", fileName);
            }
            else
                System.out.printf("No contact was imported from %s", fileName);

        } catch (FileNotFoundException e) {
            System.out.println("%s has not been found, fileName");
        }
    }

    private void importFrom() throws IOException {

        System.out.println("Enter the name of the file in format *name*.txt");
        String fileName = reader.readLine().toLowerCase();
        if (!fileName.endsWith(".txt")) {
            fileName += ".txt";
        }
        try (FileInputStream fis = new FileInputStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            String rd;
            int counter = 0;
            while ((rd = reader.readLine()) != null) {
                String[] args = rd.split("\\|");
                addContact(new Contact(args[0], args[1], args[2], new BirthDate(args[3])));
                counter++;
            }
            if (counter > 0)
                System.out.printf("%d contact(s) was(were) imported from %s.", counter, fileName);
            else
                System.out.printf("No contacts were imported from %s", fileName);
        } catch (FileNotFoundException e) {
            System.out.println("%s has not been found, fileName");
        }
    }

    private void exportOneContact() throws IOException {
        if (contactList.isEmpty()) {
            System.out.println("there is no contact to export");
        } else {
            System.out.println("Enter the name of the file in format *name*.txt");
            String fileName = reader.readLine().toLowerCase();
            if (!fileName.endsWith(".txt")) {
                fileName += ".txt";
            }
            try (FileOutputStream fos = new FileOutputStream(fileName, true);
                 PrintWriter writer = new PrintWriter(fos)) {
                int index = promptIndex(contactList);
                writer.printf("%s|%s|%s|%s\n", contactList.get(index).getSurname(), contactList.get(index).getName(), contactList.get(index).getNumber(), contactList.get(index).getbirthdate());
                System.out.println("All contacts added successfully");
            } catch (FileNotFoundException e) {
                System.out.printf("%s has not been found", fileName);
            }
        }
    }

    private void exportTo() throws IOException {
        if (contactList.isEmpty()) {
            System.out.println("there is no contact to export");
        } else {
            System.out.println("Enter the name of the file in format *name*.txt");
            String fileName = reader.readLine().toLowerCase();
            if (!fileName.endsWith(".txt")) {
                fileName += ".txt";
            }
            try (FileOutputStream fos = new FileOutputStream(fileName);
                 PrintWriter writer = new PrintWriter(fos)) {
                for (Contact contact : contactList) {
                    writer.printf("%s|%s|%s|%s\n\n", contact.getSurname(), contact.getName(), contact.getNumber(), contact.getbirthdate());
                }
                System.out.println("All contacts added successfully");
            } catch (FileNotFoundException e) {
                System.out.printf("%s has not been found", fileName);
            }
        }
    }

    private void printContactsUsingAgefilter() throws IOException, IllegalArgumentException, NumberFormatException {
        if (contactList.isEmpty()) {
            System.out.println("there is no contact");
        } else {
            int chooseage = 0;
            do {
                boolean isrightinput = false;
                while (!isrightinput) {
                    printAgeMenu();
                    System.out.println("Enter the option: ");
                    chooseage = Integer.parseInt(reader.readLine());
                    if (chooseage < 0 || chooseage > 3) {
                        throw new IllegalArgumentException("Your number should be greater than 0 and smaller than 10");
                    }
                    if (chooseage == 0) break;
                    processAgeMenuInput(chooseage);
                    isrightinput = true;
                }
            } while (chooseage != 0);
        }
    }

    private void processAgeMenuInput(int choosingage) throws IOException {
        System.out.println("please, enter the year for comparing: ");
        int yearToCompare = Integer.parseInt(reader.readLine());
        int counter = 1;

        switch (choosingage) {
            case 1:
                for (Contact contact : contactList) {
                    if (contact.getbirthdate().getYear() < yearToCompare) {
                        System.out.printf("%d)", counter++);
                        System.out.println(contact);
                    }
                }
                if (counter == 1)
                    System.out.println("There are no contacts which is older than this year");
                break;
            case 2:
                for (Contact contact : contactList) {
                    if (contact.getbirthdate().getYear() > yearToCompare) {
                        System.out.printf("%d)", counter++);
                        System.out.println(contact);
                    }
                }
                if (counter == 1)
                    System.out.println("There are no contacts which is younger than this year");
                break;
            case 3:
                for (Contact contact : contactList) {
                    if (contact.getbirthdate().getYear() == yearToCompare) {
                        System.out.printf("%d)", counter++);
                        System.out.println(contact);
                    }
                }
                if (counter == 1)
                    System.out.println("There are no contacts which have similar age as this year");
                break;
        }


    }

    private void printContactsWithPrefix() throws IOException {
        int counter = 1;
        if (contactList.isEmpty()) {
            System.out.println("there is no contact");
        } else {
            System.out.println("Enter prefix of surname:");
            String prefix = reader.readLine();
            for (Contact contact : contactList) {
                if (contact.getSurname().toLowerCase().startsWith(prefix.toLowerCase())) {
                    System.out.printf("%d)", counter++);
                    System.out.println(contact);
                }
            }
            if (counter == 1)
                System.out.println("There is no contact");
        }
    }

    private void deleteContact() throws IOException {
        if (contactList.isEmpty()) {
            System.out.println("there is no contact");
        } else {
            System.out.println("Enter the number of contact, which you want to delete");
            int dindex = promptIndex(contactList);
            contactList.remove(dindex);
        }
    }

    private void seeContacts(List<Contact> arrayList) {
        if (arrayList.isEmpty()) {
            System.out.println("There is no contacts");
        } else {
            System.out.println();
            for (Contact contact : arrayList) {
                System.out.println((arrayList.indexOf(contact) + 1) + ") " + contact);
            }
        }
    }

    private void addContact(Contact newContact) {
        contactList.add(newContact);
        Collections.sort(contactList);
        System.out.println("Contact added successfully!!!");
    }

    private Contact promptContact() throws IOException {
        Contact c = new Contact();
        System.out.print("Enter the surname: ");
        c.setSurname(reader.readLine());
        System.out.print("Enter your name: ");
        c.setName(reader.readLine());
        System.out.print("Enter your number: ");
        c.setNumber(reader.readLine());
        System.out.print("Enter your birthdate in format dd.mm.yyyy: ");
        BirthDate bd = new BirthDate(reader.readLine());
        c.setBirthdate(bd);
        return c;
    }


    private void updateContactinfo() throws IOException, NumberFormatException, IllegalArgumentException {
        if (contactList.isEmpty()) {
            System.out.println("Add some contacts");
        } else {

            int choice = 0;
            do {
                boolean checker = false;
                while (!checker) {
                    printChangeMenu();
                    System.out.print("Choose the number from the menu above: ");
                    choice = Integer.parseInt(reader.readLine());
                    if (choice == 0)
                        break;

                    if (choice < 0 || choice > 4) {
                        throw new IllegalArgumentException("Your number should be greater than 0 and smaller than 10");
                    }

                    System.out.println("Enter the number of contact");
                    int index = promptIndex(contactList);
                    checker = true;
                    processChangeMenuInput(index, choice);
                }
            }
            while (choice != 0);
        }
    }

    private void processChangeMenuInput(int indexContact, int choice) throws IOException {
        switch (choice) {
            case 1:
                System.out.println("Enter new surname: ");
                contactList.get(indexContact).setSurname(reader.readLine());
                System.out.println("You updated surname successfully");
                break;

            case 2:
                System.out.println("Enter new name: ");
                contactList.get(indexContact).setName(reader.readLine());
                System.out.println("You updated name successfully");
                break;

            case 3:
                System.out.println("Enter new number: ");
                contactList.get(indexContact).setNumber(reader.readLine());
                System.out.println("You updated number successfully");
                break;

            case 4:
                System.out.println("Enter new Birthdate: ");
                BirthDate bs = new BirthDate(reader.readLine());
                contactList.get(indexContact).setBirthdate(bs);
                System.out.println("You updated Birthdate successfully");
                break;
        }
    }

    private int promptIndex(List<Contact> arrayList) throws IOException, NumberFormatException, IllegalArgumentException {
        int index = 0;
        boolean isIndexRight = false;
        while (!isIndexRight) {
            seeContacts(arrayList);
                System.out.println("Choose the contact: ");
            index = Integer.parseInt(reader.readLine());
            if (index > arrayList.size() || index < 1) {
                throw new IllegalArgumentException("Please, choose existing contact!");
            }
            isIndexRight = true;
        }
        return index - 1;
}
}