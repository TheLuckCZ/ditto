package cz.luck.ditto.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.time.OffsetDateTime

class CitFinDTOs {

    data class TransactionsPeriodsJsonCamp052(
        @JsonProperty("BkToCstmrAcctRpt") val bkToCstmrAcctRpt: BkToCstmrAcctRpt
    )

    data class TransactionsLastJsonCamt052(
        @JsonProperty("BkToCstmrAcctRpt") val bkToCstmrAcctRpt: BkToCstmrAcctRpt
    )

    /**
     * Bank to Customer Account Report
     */
    data class BkToCstmrAcctRpt(
        @JsonProperty("GrpHdr") val grpHdr: GrpHdr, // Group Header
        @JsonProperty("Rpt") val rpt: Rpt           // Report
    )

    /**
     * Group header
     */
    data class GrpHdr(
        @JsonProperty("MsgId") val msgId: String,
        @JsonProperty("CreDtTm") val creDtTm: OffsetDateTime       // Creation Date Time
        // TODO: Could be extended if needed - see: api-clients/citfin-api/src/main/resources/819-standard-pro-elektronicky-prubezny-vypis-xml-camt-052-v2018.pdf
        //  Right now: Ain't nobody got time for that!
        // @JsonProperty("MsgRcpt") val msgRcpt: MsgRcpt?,         // Message Recipient
        // @JsonProperty("MsgPgntn") val msgPgntn: MsgPgntn?,      // Message Pagination
        // @JsonProperty("PgNb") val pgNb: PgNb,                   // Page Number
        // @JsonProperty("LastPgInd") val lastPgInd: LastPgInd,    // Last Page Indicator
    )

    /**
     * Report
     */
    data class Rpt(
        @JsonProperty("Id") val id: String,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        @JsonProperty("CreDtTm") val creDtTm: OffsetDateTime,    // Creation Date Time
        @JsonProperty("FrToDt") val frToDt: FrToDt?,             // From - To Date
        @JsonProperty("Acct") val acct: Acct,                    // Account
        @JsonProperty("Bal") val bal: List<Bal> = emptyList(),   // Balance (s?)
        @JsonProperty("Ntry") val ntry: List<Ntry> = emptyList() // Entry
    )

    /**
     * From - To Date
     */
    data class FrToDt(
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        @JsonProperty("FrDtTm") val frDtTm: OffsetDateTime, // From Date
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        @JsonProperty("ToDtTm") val toDtTm: OffsetDateTime  // To Date
    )

    /**
     * Account
     */
    data class Acct(
        @JsonProperty("Id") val id: Id,
        @JsonProperty("Ccy") val ccy: String    // Currency CZK, EUR, ETC...
    )

    /**
     * Identification
     */
    data class Id(
        @JsonProperty("Item") val item: Item
    )

    /**
     *  Item "additional info tag": usually used with Item of type: GenericAccountIdentification1
     */
    data class Item(
        @JsonProperty("\$type") val type: String,  // GenericAccountIdentification1
        @JsonProperty("Id") val id: String
    )

    /**
     * Balance
     */
    data class Bal(
        @JsonProperty("Tp") val tp: Tp,                     // Type: BalanceType12CZ whatever that is
        @JsonProperty("Amt") val amt: Amt,                  // Amount
        @JsonProperty("CdtDbtInd") val cdtDbtInd: String,   // CreditDebitCode (Debit (DBIT) / kredit (CRDT)
        @JsonProperty("Dt") val dt: Dt                      // Date
    )

    data class Tp(
        @JsonProperty("CdOrPrtry") val cdOrPrtry: CdOrPrtry
    )

    /**
     * Code or Proprietary
     *
     * BalanceType12Code:
     * CLAV ClosingAvailable Disponibilní zůstatek
     * PRCD PreviouslyClosedBooked Počáteční zůstatek
     * CLBD ClosingBooked Konečný zůstatek
     * ITBD InterimBooked Průběžný zůstatek
     */
    data class CdOrPrtry(
        @JsonProperty("Item") val item: String // Should be Cd (of type BalanceType12Code) but in payload is "Item" as String
    )

    /**
     * Amount:
     * Currency and Value
     */
    data class Amt(
        @JsonProperty("Ccy") val ccy: String,
        @JsonProperty("Value") val value: String = ""
    )

    /**
     * Date (supposedly ISODate whatever CitFin means by it)
     */
    data class Dt(
        @JsonProperty("Item") val item: String = "",                        // Based on payload will have format: YYYY-MM-DD
        @JsonProperty("ItemElementName") val itemElementName: String = ""   // Probably will be just set to "Dt"
    )

    /**
     * Entry
     *
     * EntryStatus2Code:
     * BOOK Booked Účetní status
     * PDNG Pending V procesu
     * INFO Information Pouze pro informační účely
     */
    data class Ntry(
        @JsonProperty("NtryRef") var ntryRef: String = "",          // Unique entry reference
        @JsonProperty("Amt") val amt: Amt,                          // Amount
        @JsonProperty("CdtDbtInd") val cdtDbtInd: String,           // CreditDebitCode (Debit (DBIT) / kredit (CRDT)
        @JsonProperty("RvslInd") val rvslInd: Boolean,              // Reversal indicator (True == Storno)
        @JsonProperty("Sts") val sts: String = "",                  // Status EntryStatus2Code
        @JsonProperty("BookgDt") val bookgDt: Dt,                   // Date of "Zaúčtování"
        @JsonProperty("ValDt") val valDt: Dt,                       // Value Date: Datum of "Splatnosti"
        @JsonProperty("BkTxCd") val bkTxCd: BkTxCd,                 // Bank transaction code
        @JsonProperty("NtryDtls") val ntryDtls: List<NtryDtls>?     // Entry Details (details of transaction)
    )

    /**
     * Bank transaction code
     */
    data class BkTxCd(
        @JsonProperty("Prtry") val prtry: Prtry
    )

    /**
     * Proprietary (tx identification in format ProprietaryBankTransactionCodeStructure1
     */
    data class Prtry(
        @JsonProperty("Cd") val cd: String = "",
        @JsonProperty("Issr") val issr: String?,    // Issuer (Could be "Czech Banking Association" or "CBA")
    )

    /**
     * Entry Details (details of transaction)
     */
    data class NtryDtls(
        @JsonProperty("TxDtls") val txDtls: List<TxDtls>?,  // Transaction details (list)
    )

    /**
     * Transaction details
     */
    data class TxDtls(
        @JsonProperty("Refs") val refs: Refs?,                      // References
        @JsonProperty("RltdPties") val rltdPties: RltdPties?,       // Related Parties
        @JsonProperty("RmtInf") val rmtInf: RmtInf?,                // Remittance Information (Informace o účelu platby)
        @JsonProperty("AddtlTxInf") val addtlTxInf: AddtlTxInf?     // Additional Tx Information
    )

    /**
     * Unfortunately they are sending us this:
     *
     * "AddtlTxInf": "{\"operationTypeName\":\"Příchozí platba tuzemská\",\"paymentPriority\":\"NO\"}"
     *
     * Meaning in the future they will probably fix it, and would break out parsing... so forward compatibility it is.
     */
    @JsonDeserialize(using = AddtlTxInfDeserializer::class)
    data class AddtlTxInf(
        @JsonProperty("operationTypeName") val operationTypeName: String?,  // Operation type name
        @JsonProperty("paymentPriority") val paymentPriority: String?       // Payment priority
    )

    /**
     * Reference for different kinds of payments
     *
     * - EndToEndId -
     * ZPS: Klientsk reference,
     * SEPA: Reference,
     * Karetní operace: Číslo dobíjeného mobilu, případně číslo faktury,
     * Klientská reference Dříve i pro TPS: Variabilní symbol
     */
    data class Refs(
        @JsonProperty("AcctSvcrRef") val acctSvcrRef: String = "",  // Bankovní reference [Max35Text]
        @JsonProperty("PmtInfId") val pmtInfId: String?,            // Payment Information Identification
        @JsonProperty("EndToEndId") val endToEndId: String?,        // Usually Variabilni symbol [Max35Text]
        @JsonProperty("InstrId") val instrId: String?               // ZPS Instruction Identification
    )

    /**
     * Related Parties
     */
    data class RltdPties(
        @JsonProperty("Dbtr") val dbtr: Dbtr?,                  // Debtor AKA Plátce
        @JsonProperty("DbtrAcct") val dbtrAcct: DbtrAcct?,      // Debtor account AKA Účet Plátce
        @JsonProperty("CdtrAcct") val cdtrAcct: CdtrAcct?       // Creditor account AKA Účet Příjemce
    )

    /**
     * Debtor AKA Plátce
     */
    data class Dbtr(
        @JsonProperty("Nm") val nm: String? // Name
    )

    /**
     * Debtor account AKA Účet Plátce
     */
    data class DbtrAcct(
        @JsonProperty("Id") val id: Id
    )

    /**
     * Creditor account AKA Účet Příjemce
     */
    data class CdtrAcct(
        @JsonProperty("Id") val id: Id
    )

    /**
     * Remittance Information (Informace o účelu platby)
     *
     * Ustrd : "Unstructured"
     */
    data class RmtInf(
        @JsonProperty("Ustrd") val ustrd: List<String> = emptyList()    // List of [Max140Text]
    )

}
