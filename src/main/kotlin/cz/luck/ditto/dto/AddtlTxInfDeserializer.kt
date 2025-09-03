package cz.luck.ditto.dto

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jsonMapper
import cz.luck.ditto.dto.CitFinDTOs.AddtlTxInf

class AddtlTxInfDeserializer : JsonDeserializer<AddtlTxInf?>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): AddtlTxInf? {
        val node: JsonNode = p.codec.readTree(p)
        if (node.isTextual) { // If the node is already a JSON object, map it directly from JSON node textValue
            val info = jsonMapper().readValue(node.textValue(), AddtlTxInf::class.java)
            return info
        } else { // If the node is a plain string, parse it manually from node values and remove the escaping
            val info = AddtlTxInf(
                node.get("operationTypeName").toString().replace("\"", ""),
                node.get("paymentPriority").toString().replace("\"", "")
            )
            return info
        }
    }

}