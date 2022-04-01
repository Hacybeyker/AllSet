package com.hacybeyker.allset.view.recycler.viewtype.data

import com.hacybeyker.allset.view.recycler.viewtype.vo.HobieVO
import com.hacybeyker.allset.view.recycler.viewtype.vo.PersonVO
import com.hacybeyker.allset.view.recycler.viewtype.vo.TypePersonVO

/**
 * Created by Carlos Osorio on 19/07/2021
 */

class PersonData {
    companion object {
        fun fetchPersons(): ArrayList<PersonVO> {
            return arrayListOf(
                PersonVO(
                    name = "Carlos",
                    age = 30,
                    type = TypePersonVO.HARD,
                    hobbies = arrayListOf(
                        HobieVO(name = "cine", description = "mirar peliculas"),
                        HobieVO(name = "trekking", description = "ir de paseo al campo")
                    )
                ),
                PersonVO(
                    name = "Jorgue",
                    age = 45,
                    type = TypePersonVO.NORMAL
                ),
                PersonVO(
                    name = "Fernando",
                    age = 18,
                    type = TypePersonVO.HARD,
                    hobbies = arrayListOf(
                        HobieVO(name = "cine", description = "mirar peliculas"),
                        HobieVO(name = "trekking", description = "ir de paseo al campo"),
                        HobieVO(name = "comics", description = "leer comics")
                    )
                )

            )

        }
    }
}