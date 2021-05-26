package com.g1e1.adoptgram.Data

object DataManager {
    val especies = ArrayList<Especie>()
    val posts = ArrayList<Post>()

    init {
        this.initializeEspecie()
    }

    private fun initializeEspecie(){
        var especie =  Especie(1,"Perros")
        especies.add(especie)

        especie = Especie(2,"Gatos")
        especies.add(especie)

        especie = Especie(3,"Peces")
        especies.add(especie)

        especie = Especie(4,"Aves")
        especies.add(especie)

        especie = Especie(5,"Hámsters")
        especies.add(especie)
    }

}