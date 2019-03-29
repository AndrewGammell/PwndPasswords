package menu;

import java.util.Scanner;

import services.PwnageServices;

public class MainMenu {

	private boolean exit = false;
	private String chose;
	private Scanner scanner = new Scanner(System.in);
	private PwnageServices service = new PwnageServices();

	public void runMenu() {
		System.out.println("Welcome To Pwnd Passwords Main Menu");
		do {

			showOptions();

			chose = scanner.next();

			doOperation(chose);			

		}while(!exit);

	}

	private void showOptions() {

		System.out.println("Please make your selection now"
				+ "\n1) List all pwnd sites"
				+ "\n2) Check password for Pwnage"
				+ "\n3) Check account for pwnage"
				+ "\nh) Show Help"
				+ "\ne) Exit Application"
				+ "\n"
				);
	}

	private void doOperation(String chose) {

		switch(chose.toLowerCase()) {
		case "1": System.out.println("printing list of pwnd site\n"); service.breachService();
		break;
		case "2": service.passwordService();
		break;
		case "3": service.accountService();
		break;
		case "e": System.out.println("Good Bye"); exit = true;
		break;
		case "h": showHelp();
		break;
		default: System.out.println("Sorry you made an invalid selection\n");
		}
	}
	
	private void showHelp() {
		System.out.println("help section is under construction");
	}
}
