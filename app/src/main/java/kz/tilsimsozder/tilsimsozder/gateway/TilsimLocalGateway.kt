package kz.tilsimsozder.tilsimsozder.gateway

interface TilsimLocalGateway {
    fun getTilsimContent(): List<String>
}