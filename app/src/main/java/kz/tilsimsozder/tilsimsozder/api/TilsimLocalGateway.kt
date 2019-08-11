package kz.tilsimsozder.tilsimsozder.api

interface TilsimLocalGateway {
    fun getTilsimContent(): List<String>
}