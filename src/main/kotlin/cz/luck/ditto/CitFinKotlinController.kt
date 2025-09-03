package cz.luck.ditto

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import cz.luck.ditto.dto.CitFinDTOs
import cz.luck.ditto.dto.CitFinDTOs.BkToCstmrAcctRpt
import cz.luck.ditto.dto.CitFinDTOs.DbtrAcct
import cz.luck.ditto.dto.CitFinDTOs.TransactionsLastJsonCamt052
import java.nio.file.Files
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(path = ["/kotlin"])
class CitFinKotlinController(
    val citFinService: CitFinService
) {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    var latestTxDate: Date = Date.from(LocalDate.of(2023, 10, 9).atStartOfDay(ZoneId.systemDefault()).toInstant())
    var latestTxId: Int = 11

    @GetMapping("/test")
    fun getTestMessage(): ResponseEntity<String> {
        val report: TransactionsLastJsonCamt052 = citFinService.processJsonResponse(
            //payloadFromFile("test.json")
            payloadFromFile("CITFIN.json-camt052-2024.json")
        )
        return ResponseEntity.ok(
            report.toString()
        )
    }

    // GET - Transactions
    @RequestMapping(
        value = ["/last/{token}/transactions.{format}"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getLastTransactions(
        @PathVariable("token") token: String,
        @PathVariable("format") format: String
    ): ResponseEntity<String> {

        // parse file into DTO
        // Filter DTO based on tx dates and tx ids
        // and return as string, the filtered transactions
        val mapper = jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

        val txsInsideDto = mapper.readValue(payloadFromFile("CITFIN.json-camt052-2024.json"), TransactionsLastJsonCamt052::class.java)

        return ResponseEntity.ok(
            mapper.writeValueAsString(
                filterTxsDto(txsInsideDto)
            )
        )
    }

    @RequestMapping(
        value = ["/periods/{token}/{from}/{until}/transactions.{format}"],
        method = [RequestMethod.GET]
    )
    fun getPeriodsTransactions(
        @PathVariable("token") token: String,
        @PathVariable("from") from: String,   // Example: 2024-10-09
        @PathVariable("until") until: String, // Example: 2024-10-09
        @PathVariable("format") format: String
    ): ResponseEntity<String> {
        return ResponseEntity.ok("Called.")
    }

    // PUT - Tokens
    @RequestMapping(
        value = ["/set-last-id/{token}/{transaction_id}.{format}"],
        method = [RequestMethod.PUT]
    )
    fun setLastId(
        @PathVariable("token") token: String,
        @PathVariable("transaction_id") transactionId: Int,
        @PathVariable("format") format: String
    ): ResponseEntity<String> {
        this.latestTxId = transactionId
        return ResponseEntity.ok("Last Tx ID se to: $transactionId")
    }

    @RequestMapping(
        value = ["/set-last-date/{token}/{date}.{format}"],
        method = [RequestMethod.PUT]
    )
    fun setLastDate(
        @PathVariable("token") token: String,
        @PathVariable("date") date: String, // Example: 2024-10-09
        @PathVariable("format") format: String,
    ): ResponseEntity<String> {
        this.latestTxDate = Date.from(LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay(ZoneId.systemDefault()).toInstant())
        return ResponseEntity.ok("Last Tx DATE se to: $date")
    }





    // Util methods

    fun filterTxsDto(input: TransactionsLastJsonCamt052): TransactionsLastJsonCamt052 {
        // Mirny prekryv transakci
        latestTxId -= 10
        val currentIntervalFirst = latestTxId.toString()

        input.bkToCstmrAcctRpt.rpt.ntry.forEach { entry ->
            entry.ntryRef = (++latestTxId).toString()
        }

        /*
        val modifiedInput: TransactionsLastJsonCamt052 = TransactionsLastJsonCamt052(
            BkToCstmrAcctRpt(
                input.bkToCstmrAcctRpt.grpHdr,
                CitFinDTOs.Rpt(
                    input.bkToCstmrAcctRpt.rpt.id,
                    input.bkToCstmrAcctRpt.rpt.creDtTm,
                    input.bkToCstmrAcctRpt.rpt.frToDt,
                    input.bkToCstmrAcctRpt.rpt.acct,
                    input.bkToCstmrAcctRpt.rpt.bal,
                    modified
                )
            )
        )
        */

        val currentIntervalLast = latestTxId.toString()

        log.info("Current FIRST - LAST: $currentIntervalFirst - $currentIntervalLast")

        return input
    }

    fun payloadFromFile(file: String): String {
        val resource = ClassPathResource(file)
        val path = resource.file.toPath()
        return Files.readString(path)
    }

    fun getMockNtries(): List<CitFinDTOs.Ntry> {
        val mockNtryList = listOf(
            CitFinDTOs.Ntry(
                ntryRef = (++latestTxId).toString(),
                amt = CitFinDTOs.Amt(ccy = "CZK", value = "9103.65"),
                cdtDbtInd = "CRDT",
                rvslInd = false,
                sts = "BOOK",
                bookgDt = CitFinDTOs.Dt(item = "2024-11-12", itemElementName = "Dt"),
                valDt = CitFinDTOs.Dt(item = "2024-11-12", itemElementName = "Dt"),
                bkTxCd = CitFinDTOs.BkTxCd(prtry = CitFinDTOs.Prtry(cd = "203", issr = null)),
                ntryDtls = listOf(
                    CitFinDTOs.NtryDtls(
                        txDtls = listOf(
                            CitFinDTOs.TxDtls(
                                refs = CitFinDTOs.Refs(
                                    acctSvcrRef = "87331499",
                                    null, null, null
                                ),
                                rltdPties = CitFinDTOs.RltdPties(
                                    dbtr = CitFinDTOs.Dbtr(
                                        nm = "Lukáš Franz"
                                    ),
                                    dbtrAcct = DbtrAcct(
                                        id = CitFinDTOs.Id(
                                            item = CitFinDTOs.Item(
                                                id = "1005770017/3030",
                                                type = "GenericAccountIdentification1"
                                            )
                                        )
                                    ),
                                    cdtrAcct = CitFinDTOs.CdtrAcct(
                                        id = CitFinDTOs.Id(
                                            item = CitFinDTOs.Item(
                                                id = "1005770017/3030",
                                                type = "GenericAccountIdentification1"
                                            )
                                        )
                                    )
                                ),
                                rmtInf = null,
                                addtlTxInf = CitFinDTOs.AddtlTxInf(
                                    operationTypeName = "Příchozí platba tuzemská",
                                    paymentPriority = "NO"
                                )
                            )
                        )
                    )
                )
            ),
            CitFinDTOs.Ntry(
                ntryRef = (++latestTxId).toString(),
                amt = CitFinDTOs.Amt(ccy = "CZK", value = "1000.00"),
                cdtDbtInd = "CRDT",
                rvslInd = false,
                sts = "BOOK",
                bookgDt = CitFinDTOs.Dt(item = "2024-11-12", itemElementName = "Dt"),
                valDt = CitFinDTOs.Dt(item = "2024-11-12", itemElementName = "Dt"),
                bkTxCd = CitFinDTOs.BkTxCd(prtry = CitFinDTOs.Prtry(cd = "203", issr = null)),
                ntryDtls = listOf(
                    CitFinDTOs.NtryDtls(
                        txDtls = listOf(
                            CitFinDTOs.TxDtls(
                                refs = CitFinDTOs.Refs(acctSvcrRef = "87331592",
                                    null, null, null),
                                rltdPties = CitFinDTOs.RltdPties(
                                    dbtr = CitFinDTOs.Dbtr(nm = "VIKTOR ABEL"),
                                    dbtrAcct = DbtrAcct(
                                        id = CitFinDTOs.Id(
                                            item = CitFinDTOs.Item(
                                                id = "1005770017/3030",
                                                type = "GenericAccountIdentification1"
                                            )
                                        )
                                    ),
                                    cdtrAcct = CitFinDTOs.CdtrAcct(
                                        id = CitFinDTOs.Id(
                                            item = CitFinDTOs.Item(
                                                id = "1005770017/3030",
                                                type = "GenericAccountIdentification1"
                                            )
                                        )
                                    )
                                ),
                                rmtInf = CitFinDTOs.RmtInf(ustrd = listOf("Viktor Abel")),
                                addtlTxInf = CitFinDTOs.AddtlTxInf(
                                    operationTypeName = "Příchozí platba tuzemská",
                                    paymentPriority = "YES"
                                )
                            )
                        )
                    )
                )
            )
        )
        return mockNtryList
    }

}
