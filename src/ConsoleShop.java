import java.util.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;

class ConsoleShop {
    static void initialise() {
        System.out.println("============MAIN MENU============\n" +
                "Type \"1\" to add a new product.\n" +
                "Type \"2\" to read an existing product.\n" +
                "Type \"3\" to edit an existing product.\n" +
                "Type \"4\" to read all products.\n" +
                "Type \"5\" to delete a product.\n" +
                "Type \"6\" to search for a product.\n" +
                "Type \"exit\" to exit the application.\n");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            if (Objects.equals(command, "1")) {
                System.out.println("============ADD PRODUCT============\n");
                addProduct();
            } else if (Objects.equals(command, "2")) {
                System.out.println("============READ PRODUCT============\n");
                readProduct();
            } else if (Objects.equals(command, "3")) {
                System.out.println("============EDIT PRODUCT============\n");
                editProduct();
            } else if (Objects.equals(command, "4")) {
//                System.out.println("============LIST PRODUCTS============\n");
                listProducts();
            } else if (Objects.equals(command, "5")) {
                System.out.println("============DELETE PRODUCT============\n");
                deleteProduct();
            } else if (Objects.equals(command, "6")) {
                System.out.println("============SEARCH PRODUCT============\n");
                searchProduct();
            } else if (Objects.equals(command, "exit")) {
                System.out.println("Exiting program");
                System.exit(0);
            } else {
                System.out.println("INVALID COMMAND");
            }
        }
    }

    private static void addProduct() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("You have chosen to add a new product. Press \"enter\" to continue. Type anything to return.");
        while (true) {
            String command = scanner.nextLine();
            if (!Objects.equals(command, "")) {
                System.out.println("Returning to main menu.");
                initialise();
            } else {
                break;
            }
        }
        System.out.println("Enter product id (barcode)");
        int id;
        while (true) {
            try {
                id = Integer.parseInt(scanner.nextLine());
                break;
            } catch (Exception ex) {
                System.out.println("Invalid id");
            }
        }


        System.out.println("Enter product brand");
        String brand = scanner.nextLine();

        System.out.println("Enter product model");
        String model = scanner.nextLine();

        System.out.println("Enter product description");
        String description = scanner.nextLine();

        System.out.println("Enter product price");
        double price;
        while (true) {
            try {
                price = Double.parseDouble(scanner.nextLine());
                price = Math.round(price * 100);
                price /= 100;
                break;
            } catch (Exception ex) {
                System.out.println("Invalid price");
            }
        }


        //PRODUCT INFO CONFIRMATION:
        System.out.println(
                "Are you sure you want to add the following product?\n" +
                        "id (barcode): " + id + "\n" +
                        "Brand: " + brand + "\n" +
                        "Model: " + model + "\n" +
                        "Description: " + description + "\n" +
                        "Price: " + price + "\n"
        );
        System.out.println("Press \"enter\" to continue. Type anything to return.");
        while (true) {
            String command = scanner.nextLine();
            if (!Objects.equals(command, "")) {
                System.out.println("Returning to main menu. If you need help, type \"help\"");
                initialise();
            } else {
                break;
            }
        }

        //CHECK IF PRODUCT ALREADY EXISTS
        String productFileString = id + ".ser";
        File productFile = new File(productFileString);

        if (productFile.exists() && !productFile.isDirectory()) {
            System.out.println("A product with this id (barcode) already exists. Do you want to change it [y/n].");
            while (true) {
                String command = scanner.nextLine();
                if (Objects.equals(command, "y") || Objects.equals(command, "yes")) {
                    try {
                        id = Integer.parseInt(scanner.nextLine());
                        break;
                    } catch (Exception ex) {
                        System.out.println("Not a valid id");
                    }
                }
            }
        }

        Product product = new Product();
        product.setId(id);
        product.setBrand(brand);
        product.setModel(model);
        product.setDescription(description);
        product.setPrice(price);


        //CREATE THE PRODUCT FILE
        serializeProduct(product);

        //RETURN TO MAIN FUNCTION
        System.out.println("Returning to main menu. If you need help, type \"help\"");
        initialise();
    }

    private static void serializeProduct(Product product) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(product.getId() + ".ser"))) {

            oos.writeObject(product);
            System.out.println("Product " + product.getId() + " Saved");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void readProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the product id (barcode). Type \"all\" to view all available products. Type \"return\" to to go to the main menu");

        int productId;
        while (true) {
            String command = scanner.nextLine();
            if (Objects.equals(command, "all")) {
                listProducts();
            } else if (Objects.equals(command, "return")) {
                System.out.println("Returning to main menu");
                initialise();
            } else {
                try {
                    productId = Integer.parseInt(command);
                    break;
                } catch (Exception e) {
                    System.out.println("INVALID ID. TRY AGAIN");
                }
            }
        }

        Product product = null;
        try (ObjectInputStream ois
                     = new ObjectInputStream(new FileInputStream(Integer.toString(productId) + ".ser"))) {

            product = (Product) ois.readObject();

        } catch (Exception ex) {
            System.out.println("This product doesn't exist. Returning to product view menu");
            readProduct();
        }

        System.out.println(product.toString());
        readProduct();
    }

    private static void editProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the product id (barcode). Type \"all\" to view all available products. Type \"return\" to to go to the main menu");

        int productId;
        while (true) {
            String command = scanner.nextLine();
            if (Objects.equals(command, "all")) {
                listProducts();
            } else if (Objects.equals(command, "return")) {
                System.out.println("Returning to main menu");
                initialise();
            } else {
                try {
                    productId = Integer.parseInt(command);
                    break;
                } catch (Exception e) {
                    System.out.println("INVALID ID. TRY AGAIN");
                }
            }
        }
        Product product = null;

        try (ObjectInputStream ois
                     = new ObjectInputStream(new FileInputStream(Integer.toString(productId) + ".ser"))) {

            product = (Product) ois.readObject();

        } catch (Exception ex) {
            System.out.println("This product doesn't exist. Returning to main menu");
            initialise();
        }

        System.out.println("Selected product is: " + product.toString());
        while (true) {
            System.out.println("Type the name of the field you want to edit. Type \"return\" when you are done");
            String command = scanner.nextLine();
            if (Objects.equals(command, "return")) {
                break;
            } else if (Objects.equals(command, "id")) {
                System.out.println("You cannot change the id (barcode). Either delete the product or create a new one.");
            } else if (Objects.equals(command, "brand")) {
                System.out.println("Enter brand (type return to go back)");
                while (true) {
                    String newParam = scanner.nextLine();
                    if (Objects.equals(newParam, "return")) {
                        break;
                    } else {
                        try {
                            product.setBrand(newParam);
                            System.out.println("Brand changed successfully.");
                            break;
                        } catch (Exception e) {
                            System.out.println("INVALID PARAMETER. TRY AGAIN");
                        }
                    }
                }
            } else if (Objects.equals(command, "model")) {
                while (true) {
                    System.out.println("Enter model (type return to go back)");
                    String newParam = scanner.nextLine();
                    if (Objects.equals(newParam, "return")) {
                        break;
                    } else {
                        try {
                            product.setModel(newParam);
                            System.out.println("Model changed successfully.");
                            break;
                        } catch (Exception e) {
                            System.out.println("INVALID PARAMETER. TRY AGAIN");
                        }
                    }
                }
            } else if (Objects.equals(command, "description")) {
                while (true) {
                    System.out.println("Enter desription (type return to go back)");
                    String newParam = scanner.nextLine();
                    if (Objects.equals(newParam, "return")) {
                        break;
                    } else {
                        try {
                            product.setDescription(newParam);
                            System.out.println("Description changed successfully.");
                            break;
                        } catch (Exception e) {
                            System.out.println("INVALID PARAMETER. TRY AGAIN");
                        }
                    }
                }
            } else if (Objects.equals(command, "price")) {
                while (true) {
                    System.out.println("Enter price (type return to go back)");
                    String newParam = scanner.nextLine();
                    if (Objects.equals(newParam, "return")) {
                        break;
                    } else {
                        try {
                            double newPrice = Double.parseDouble(newParam);
                            newPrice = Math.round(newPrice * 100);
                            newPrice /= 100;
                            product.setPrice(newPrice);
                            System.out.println("Price changed successfully.");
                            break;
                        } catch (Exception e) {
                            System.out.println("INVALID PARAMETER. TRY AGAIN");
                        }
                    }
                }
            } else {
                System.out.println("No such data field. Available fields: id, brand, model, description, price. Type return to go back.");
            }
        }
        //PRODUCT INFO CONFIRMATION:
        System.out.println(
                "Are you sure you want to save the following product?\n" +
                        "id (barcode): " + product.getId() + "\n" +
                        "Brand: " + product.getBrand() + "\n" +
                        "Model: " + product.getModel() + "\n" +
                        "Description: " + product.getDescription() + "\n" +
                        "Price: " + product.getPrice() + "\n"
        );
        System.out.println("Press \"enter\" to continue. Type anything to return.");
        while (true) {
            String command = scanner.nextLine();
            if (!Objects.equals(command, "")) {
                System.out.println("Returning to edit menu.");
                editProduct();
            } else {
                break;
            }
        }

        //CREATE THE PRODUCT FILE
        serializeProduct(product);

        //RETURN TO MAIN FUNCTION
        System.out.println("Product edited successfully. Returning to main menu. If you need help, type \"help\"");
        initialise();
    }

    private static void deleteProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the product id (barcode) to delete. Type \"all\" to view all available products. Type \"return\" to to go to the main menu");

        int productId;
        while (true) {
            String command = scanner.nextLine();
            if (Objects.equals(command, "all")) {
                listProducts();
            } else if (Objects.equals(command, "return")) {
                System.out.println("Returning to main menu");
                initialise();
            } else {
                try {
                    productId = Integer.parseInt(command);
                    break;
                } catch (Exception e) {
                    System.out.println("INVALID ID. TRY AGAIN");
                }
            }
        }
        try {

            File file = new File(Integer.toString(productId) + ".ser");

            if (file.delete()) {
                System.out.println(file.getName() + " is deleted!");
                deleteProduct();
            } else {
                System.out.println("This file doesn't exist or something prevents deletion.");
                deleteProduct();
            }

        } catch (Exception e) {

            e.printStackTrace();
            initialise();

        }
    }

    private static void listProducts() {
        Vector<String> products = new Vector<>();

        File dir = new File(".");
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith((".ser"))) {
                products.add(file.getName());
            }
        }
        if (products.size() == 0) {
            System.out.println("There are no products in the database");
        }
        for (int i = 0; i < products.size(); i++) {
            System.out.println(products.elementAt(i));
        }
    }

    private static void searchProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the search criteria for the products. \"return\" to to go to the main menu.\n Available criterias are brand/model/price");

        String criteria;
        while (true) {
            criteria = scanner.nextLine();
            if (Objects.equals(criteria, "return")) {
                System.out.println("Returning to main menu");
                initialise();
            } else if (Objects.equals(criteria, "brand") || Objects.equals(criteria, "model") || Objects.equals(criteria, "price")) {
                break;
            } else {
                System.out.println("Invalid search criteria");
                continue;
            }
        }

        System.out.println("Please type the " + criteria + " to search for. Type return to go back to main menu.");
        String searchParam;
        while (true) {
            searchParam = scanner.nextLine();
            if (Objects.equals(searchParam, "return")) {
                System.out.println("Returning to main menu");
                initialise();
            }
            break;
        }


        //Get all files
        Vector<String> products = new Vector<>();
        File dir = new File(".");
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith((".ser"))) {
                products.add(file.getName());
            }
        }
        if (products.size() == 0) {
            System.out.println("There are no products in the database. Returning to main menu");
            initialise();
        }

        //Deserialize files and check if the match the search criteria
        int foundProducts = 0;
        for (int fileId = 0; fileId < products.size(); fileId++) {
            Product product = null;
            try (ObjectInputStream ois
                         = new ObjectInputStream(new FileInputStream(products.elementAt(fileId)))) {

                product = (Product) ois.readObject();

            } catch (Exception ex) {
                System.out.println("Error performing operation.");
                initialise();
            }
            if (Objects.equals(criteria, "model") && Objects.equals(product.getModel(), searchParam)) {
                System.out.println(product.toString());
                foundProducts++;
            } else if (Objects.equals(criteria, "brand") && Objects.equals(product.getBrand(), searchParam)) {
                System.out.println(product.toString());
                foundProducts++;
            } else if (Objects.equals(criteria, "price")) {
                try {
                    double price = Double.parseDouble(searchParam);
                    price = Math.round(price * 100);
                    price /= 100;
                    if (Objects.equals(price, product.getPrice())) {
                        System.out.println(product.toString());
                        foundProducts++;
                    }
                } catch (Exception ex) {

                }
            }
        }
        if (foundProducts == 0) {
            System.out.println("No products found matching your search criteria.");
        }
        searchProduct();
    }
}