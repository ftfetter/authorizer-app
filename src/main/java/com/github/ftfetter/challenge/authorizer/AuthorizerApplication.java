package com.github.ftfetter.challenge.authorizer;

import com.github.ftfetter.challenge.authorizer.chain.OperationValidator;
import com.github.ftfetter.challenge.authorizer.chain.validator.AccountValidator;
import com.github.ftfetter.challenge.authorizer.chain.validator.TransactionValidator;
import com.github.ftfetter.challenge.authorizer.chain.validator.account.AccountInitializedValidator;
import com.github.ftfetter.challenge.authorizer.chain.validator.account.AccountNotCreatedValidator;
import com.github.ftfetter.challenge.authorizer.chain.validator.account.CardActiveValidator;
import com.github.ftfetter.challenge.authorizer.chain.validator.transaction.DoubledTransactionTransactionValidator;
import com.github.ftfetter.challenge.authorizer.chain.validator.transaction.RecentTransactionsTransactionValidator;
import com.github.ftfetter.challenge.authorizer.chain.validator.transaction.SufficientLimitTransactionValidator;
import com.github.ftfetter.challenge.authorizer.controller.AuthorizerController;
import com.github.ftfetter.challenge.authorizer.controller.AuthorizerIO;
import com.github.ftfetter.challenge.authorizer.service.AccountService;
import com.github.ftfetter.challenge.authorizer.service.TransactionService;
import com.github.ftfetter.challenge.authorizer.singleton.ActiveAccount;
import com.github.ftfetter.challenge.authorizer.strategy.OperationProcessor;
import com.github.ftfetter.challenge.authorizer.strategy.processor.AccountCreationProcessor;
import com.github.ftfetter.challenge.authorizer.strategy.processor.TransactionAuthorizationProcessor;
import com.google.gson.Gson;

import java.util.LinkedHashSet;
import java.util.Scanner;

public class AuthorizerApplication {

	public static void main(String[] args) {
		AuthorizerController controller = injectDependencies();

		while (true) {
			controller.run();
		}
	}

	private static AuthorizerController injectDependencies() {
		// Basic dependencies
		Scanner scanner = new Scanner(System.in);
		Gson gson = new Gson();

		// Validators
		LinkedHashSet<AccountValidator> accountCreationValidators = new LinkedHashSet<>();
		accountCreationValidators.add(new AccountNotCreatedValidator());

		LinkedHashSet<AccountValidator> transactionAccountValidators = new LinkedHashSet<>();
		transactionAccountValidators.add(new AccountInitializedValidator());
		transactionAccountValidators.add(new CardActiveValidator());

		LinkedHashSet<TransactionValidator> transactionValidators = new LinkedHashSet<>();
		transactionValidators.add(new SufficientLimitTransactionValidator());
		transactionValidators.add(new RecentTransactionsTransactionValidator());
		transactionValidators.add(new DoubledTransactionTransactionValidator());

		OperationValidator operationValidator = new OperationValidator(accountCreationValidators, transactionAccountValidators, transactionValidators);

		// Services
		AccountService accountService = new AccountService(ActiveAccount.getInstance(), operationValidator);
		TransactionService transactionService = new TransactionService(ActiveAccount.getInstance(), operationValidator);

		// Processors
		AccountCreationProcessor accountCreationProcessor = new AccountCreationProcessor(gson, accountService);
		TransactionAuthorizationProcessor transactionAuthorizationProcessor = new TransactionAuthorizationProcessor(gson, transactionService);

		OperationProcessor operationProcessor = new OperationProcessor(accountCreationProcessor, transactionAuthorizationProcessor);

		// IO
		AuthorizerIO authorizerIO = new AuthorizerIO(scanner);

		return new AuthorizerController(authorizerIO, operationProcessor);
	}

}
