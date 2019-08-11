package kz.tilsimsozder.tilsimsozder.api

class TilsimDatabaseGateway: TilsimLocalGateway {
    override fun getTilsimContent(): List<String> {
        return listOf("")
    }
}