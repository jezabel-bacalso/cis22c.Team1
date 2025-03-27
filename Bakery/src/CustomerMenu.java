import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BakerySystem {
Expand
BakerySystem.java
8 KB
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
Expand
BST.java
11 KB
import java.util.Objects;

public class Customer extends User {

    private String address;
    private String phone;
Expand
Customer.java
5 KB
John Smith
johnsmith@email.com
password123

Sarah Johnson
sarahjohnson@email.com
Expand
customers.txt
1 KB
public class Employee extends User {

    private boolean isManager;

    public Employee(String firstName, String lastName,
                    String email, String password,
Expand
Employee.java
2 KB
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeeMenu {
    protected PriorityQueue orderQueue;
Expand
EmployeeMenu.java
7 KB
William Thomas
williamthomas@email.com
manager1
Manager

Olivia Martinez
Expand
employees.txt
1 KB
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
Expand
FileHandler.java
12 KB
product/type/price/description/allergens/calories

Choco Bun
Pastry
2.55
A petite bun made of chocolate dough filled with chocolate chips.
Expand
products.txt
3 KB
import java.util.NoSuchElementException;

public class Queue<T> {

    private static class Node<T> {
        T data;
Expand
Queue.java
2 KB
public abstract class User {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
Expand
User.java
2 KB
import java.util.NoSuchElementException;

public class LinkedList<T> {

    private class Node {
        T data;
Expand
LinkedList.java
5 KB
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * LoginHandler centralizes logic for loading/saving user data
Expand
LoginHandler.java
7 KB
import java.util.List;
import java.util.Scanner;

public class ManagerMenu extends EmployeeMenu {

    private ProductCatalog catalog;
Expand
ManagerMenu.java
6 KB
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order implements Comparable<Order> {
Expand
Order.java
7 KB
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class PriorityQueue {
Expand
PriorityQueue.java
3 KB
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Product implements Comparable<Product> {
Expand
Product.java
5 KB
import java.util.ArrayList;
import java.util.List;

public class ProductCatalog {

    private BST<Product> byName;   // BST using compareTo => name
Expand
ProductCatalog.java
3 KB
Nguski — 2:23 AM
wait, if this is a group project, then our codes should be the same, but we can comment our name on it for individual submit right?
don't we all have to submit?
yea I thought so
we should put all of our names
I mean we still have a presentation on this so she knows who
Ke$hAv — 2:26 AM
@jezabel
@gummyworm  we think we done with the code
vii — 2:27 AM
all tests passed
were going to begin writing javadocs, and implementing snippets into the slides
Ke$hAv — 2:28 AM
import java.util.List;
import java.util.Scanner;

public class CustomerMenu {

    private Customer currentCustomer;
Expand
message.txt
10 KB
Nguski — 2:30 AM
i was thinking a zoom call so we can share the codes and the slides and record it
vii — 2:55 AM
this is a link for a UML designer for collab

https://lucid.app/lucidchart/8fd833fe-5a4e-4e8b-ab79-df6435583314/edit?viewport_loc=-260%2C19%2C2226%2C1154%2CHWEp-vi-RSFO&invitationId=inv_3e872a38-5e73-4b83-96e1-502ff808c8ef
jezabel — 2:56 AM
do we still have to do those walkthrus/scenarios?
we can fake it but i wasnt sure if we did them yet
vii — 3:00 AM
actually forget about the website i reccomended
Nguski — 3:00 AM
we cant really present until hugh gets on right? 
vii — 3:01 AM
https://app.diagrams.net/
jezabel — 3:02 AM
yeah lemme try
Nguski — 3:02 AM
the thing is due at noon right? since we got the coding part done, lets try to finish the slides for now and try to present at 9 - 10am?
also is someone going to create the diagram and shared it?
jezabel — 3:05 AM
wtf how do i use this thing
vii — 3:05 AM
yeah i asked if jezabel can open
i have NO idea
Nguski — 3:06 AM
one more time?
jezabel — 3:07 AM
we dont lose points
hell get a zero
if hes not in the presentation
Nguski — 3:08 AM
your call
jezabel — 3:08 AM
yeah hes getting up lol
Nguski — 3:09 AM
i downloaded them
jezabel — 3:09 AM
it was okay but the orders were fucked
Nguski — 3:10 AM
sounds good
jezabel — 3:10 AM
uhhh i connected it to the github but now the graph is like
Image
my dad said u can lowkey chatgpt it
ahhh i see
IT DID NOT
ITS BLANK
idk and theres TWO 
Nguski — 3:17 AM
the disrespect?!
jezabel — 3:18 AM
wait i missed the test
was it the most recent files sent on here?
okie
ill push em
where am i supposed to put the diagram  in the repo?
Nguski — 3:21 AM
i agree lowkey
highkey agree
jezabel — 3:21 AM
get paris baguettes name OUT YALLS MOUTH
Nguski — 3:21 AM
my fault
jezabel — 3:21 AM
SAFEWAY
a whole buncha wrong takes from u
wait when i try to share the uml so yall can see it,, it takes me to the github collaborators list
Nguski — 3:23 AM
what is your go to bagel?
jezabel — 3:24 AM
https://app.diagrams.net/?src=about#Hjezabel-bacalso%2Fcis22c.Team1%2Fmain%2FBakery%2Fsrc%2FUntitled%20Diagram.drawio#%7B"pageId"%3A"Rl8gvy1_XRkPPRttpnPP"%7D
Nguski — 3:24 AM
I created a diagaram with the link you sent me
https://drive.google.com/file/d/11Wztx3JBrcyDwY3nF7owjwBOz-mQScfd/view?usp=sharing
Ke$hAv — 3:24 AM
its private i cant see it
Nguski — 3:24 AM
i updated, mb 
https://drive.google.com/file/d/11Wztx3JBrcyDwY3nF7owjwBOz-mQScfd/view?usp=sharing
﻿
import java.util.List;
import java.util.Scanner;

public class CustomerMenu {

    private Customer currentCustomer;
    private ProductCatalog catalog;
    private PriorityQueue orderQueue;
    private List<Order> allOrders;
    private Scanner sc;

    public CustomerMenu(Customer currentCustomer,
                        ProductCatalog catalog,
                        PriorityQueue orderQueue,
                        List<Order> allOrders)
    {
        this.currentCustomer = currentCustomer;
        this.catalog = catalog;
        this.orderQueue = orderQueue;
        this.allOrders = allOrders;
        this.sc = new Scanner(System.in);
    }

    public void showMenu() {
        boolean keepGoing = true;
        while (keepGoing) {
            System.out.println("\n=== Customer Menu ===");
            System.out.println("1) Search for a product (by partial name/category)");
            System.out.println("2) Find & display one record by primary key (name)");
            System.out.println("3) Find & display one record by secondary key (price)");
            System.out.println("4) List all products by name");
            System.out.println("5) List all products by price");
            System.out.println("6) Place an Order");
            System.out.println("7) View Purchases (shipped + unshipped)");
            System.out.println("8) View Shipped Orders");
            System.out.println("9) View Unshipped Orders");
            System.out.println("10) Quit (return to main menu)");
            // Added prompt at the end
            System.out.print("Choice: ");

            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1":
                    searchProduct();
                    break;
                case "2":
                    findByPrimaryKey();
                    break;
                case "3":
                    findBySecondaryKey();
                    break;
                case "4":
                    listByName();
                    break;
                case "5":
                    listByPrice();
                    break;
                case "6":
                    placeOrder();
                    break;
                case "7":
                    viewPurchases();
                    break;
                case "8":
                    viewShipped();
                    break;
                case "9":
                    viewUnshipped();
                    break;
                case "10":
                    keepGoing = false;
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void searchProduct() {
        System.out.print("Enter partial product name or category: ");
        String kw = sc.nextLine().toLowerCase();
        List<Product> allByName = catalog.getAllByName();
        boolean foundAny = false;
        for (Product p : allByName) {
            if (p.getName().toLowerCase().contains(kw)
                || p.getCategory().toLowerCase().contains(kw))
            {
                System.out.println("  " + p);
                foundAny = true;
            }
        }
        if (!foundAny) {
            System.out.println("No matching products found.");
        }
    }

    private void findByPrimaryKey() {
        System.out.print("Enter product name (exact): ");
        String name = sc.nextLine().trim();
        Product found = catalog.findByName(name);
        if (found == null) {
            System.out.println("No product with that name.");
        } else {
            System.out.println("Found: " + found);
        }
    }

    private void findBySecondaryKey() {
        System.out.print("Enter product price: ");
        double val;
        try {
            val = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException ex) {
            System.out.println("Invalid numeric input.");
            return;
        }
        Product found = catalog.findByExactPrice(val);
        if (found == null) {
            System.out.println("No product with that price.");
        } else {
            System.out.println("Found: " + found);
        }
    }

    private void listByName() {
        List<Product> list = catalog.getAllByName();
        if (list.isEmpty()) {
            System.out.println("No products in catalog.");
            return;
        }
        System.out.println("== Products (by name) ==");
        for (Product p : list) {
            System.out.println("  " + p);
        }
    }

    private void listByPrice() {
        List<Product> list = catalog.getAllByPrice();
        if (list.isEmpty()) {
            System.out.println("No products in catalog.");
            return;
        }
        System.out.println("== Products (by price) ==");
        for (Product p : list) {
            System.out.println("  " + p);
        }
    }

    private void placeOrder() {
        System.out.print("Enter product name to order: ");
        String name = sc.nextLine().trim();
        Product found = catalog.findByName(name);
        if (found == null) {
            System.out.println("No product by that name.");
            return;
        }
        System.out.print("Enter quantity: ");
        int qty;
        try {
            qty = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException ex) {
            System.out.println("Invalid quantity.");
            return;
        }
        if (qty <= 0) {
            System.out.println("Quantity must be > 0.");
            return;
        }
        if (qty > found.getStock()) {
            System.out.println("Not enough stock.");
            return;
        }

        System.out.println("Select shipping speed:");
        System.out.println("  1) OVERNIGHT");
        System.out.println("  2) RUSH");
        System.out.println("  3) STANDARD");
        String sChoice = sc.nextLine().trim();
        Order.ShippingSpeed speed;
        switch (sChoice) {
            case "1": speed = Order.ShippingSpeed.OVERNIGHT; break;
            case "2": speed = Order.ShippingSpeed.RUSH; break;
            default:  speed = Order.ShippingSpeed.STANDARD; break;
        }

        // Construct new order
        Order newOrder = new Order(currentCustomer.getEmail(), speed);
        // Add item
        Order.OrderItem item = new Order.OrderItem(
            found.getId(), found.getName(), qty, found.getPrice()
        );
        newOrder.addItem(item);

        // decrement product stock
        found.setStock(found.getStock() - qty);

        // add to queue + allOrders
        orderQueue.insert(newOrder);
        allOrders.add(newOrder);
        currentCustomer.addUnshippedOrder(newOrder);

        System.out.println("Order placed: " + newOrder);
    }

    private void viewPurchases() {
        System.out.println("== All Purchases for " + currentCustomer.getEmail() + " ==");
        LinkedList<Order> unshipped = currentCustomer.getUnshippedOrders();
        LinkedList<Order> shipped = currentCustomer.getShippedOrders();

        System.out.println("Unshipped Orders:");
        if (unshipped.isEmpty()) {
            System.out.println("  none");
        } else {
            unshipped.positionIterator();
            while (!unshipped.offEnd()) {
                System.out.println("  " + unshipped.getIterator());
                unshipped.advanceIterator();
            }
        }

        System.out.println("Shipped Orders:");
        if (shipped.isEmpty()) {
            System.out.println("  none");
        } else {
            shipped.positionIterator();
            while (!shipped.offEnd()) {
                System.out.println("  " + shipped.getIterator());
                shipped.advanceIterator();
            }
        }
    }

    private void viewShipped() {
        LinkedList<Order> shipped = currentCustomer.getShippedOrders();
        if (shipped.isEmpty()) {
            System.out.println("No shipped orders.");
            return;
        }
        System.out.println("== Shipped Orders ==");
        shipped.positionIterator();
        while (!shipped.offEnd()) {
            System.out.println("  " + shipped.getIterator());
            shipped.advanceIterator();
        }
    }

    private void viewUnshipped() {
        LinkedList<Order> unshipped = currentCustomer.getUnshippedOrders();
        if (unshipped.isEmpty()) {
            System.out.println("No unshipped orders.");
            return;
        }
        System.out.println("== Unshipped Orders ==");
        unshipped.positionIterator();
        while (!unshipped.offEnd()) {
            System.out.println("  " + unshipped.getIterator());
            unshipped.advanceIterator();
        }
    }
}
