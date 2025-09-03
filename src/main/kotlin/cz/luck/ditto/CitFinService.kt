package cz.luck.ditto

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import cz.luck.ditto.dto.CitFinDTOs.TransactionsLastJsonCamt052
import org.springframework.stereotype.Service

@Service
class CitFinService {

    fun processJsonResponse(payload: String): TransactionsLastJsonCamt052 {
        val mapper = jacksonObjectMapper().registerModule(JavaTimeModule())
        // Disable WRITE_DATES_AS_TIMESTAMPS to ensure ISO-8601 format is used
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        val report: TransactionsLastJsonCamt052 = mapper.readValue(payload, TransactionsLastJsonCamt052::class.java)

        return report
    }





}