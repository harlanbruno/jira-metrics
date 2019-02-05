package com.devfuncionar.jirametrics.util;

public interface Constants {

	public interface Encoding {

		public static final String UTF8 = "UTF-8";
	}

	public interface Category {

		public interface Credit {

			public static final String RECEIVED_CIELO = "RECEBIDO CIELO";
			public static final String BANK_TICKET = "ENTRADA COBRANCA/BOLETO";
			public static final String RECEIVED_VINDI = "RECEBIDO CREDITO VINDI";
			public static final String TRANSF_BETWEEN_ACCOUNTS = "TRANSFERENCIA ENTRE CONTAS";
			public static final String REPAYMENT = "ESTORNO";
			public static final String DEPOSIT = "DEPOSITO/TRANSFERENCIA";
			public static final String FINANCIAL_APPLICATION = "APLICACAO FINANCEIRA";
			public static final String PURCHASE_DEBIT = "COMPRA NO DEBITO";
			public static final String OTHER = "OUTROS";
		}

		public interface Debit {

			public static final String BILL_PAYMENT = "PAGAMENTO DE CONTA";
			public static final String BANK_RATE = "TARIFA BANCARIA";
			public static final String PURCHASE_IN_DEBT = "COMPRA NO DEBITO";
			public static final String BANK_MAINTENANCE = "MANUTENCAO BANCARIA";
			public static final String TRANSF_BETWEEN_ACCOUNTS = "TRANSFERENCIA ENTRE CONTAS";
			public static final String OTHER = "OUTROS";
		}
	}
}
