package kz.tilsimsozder.tilsimsozder.gateway

class TilsimDatabaseGateway: TilsimLocalGateway {
    override fun getTilsimContent(): List<String> {
        return listOf("")
    }
}