import java.io.File
import kotlin.system.measureTimeMillis

private const val TAVERN_MASTER = "Taernyl"

private const val TAVERN_NAME = "$TAVERN_MASTER's Folly"


private val firstNames = setOf("Alex", "Mordoc", "Sophie", "Tariq")
private val lastNames = setOf("Ironfoot", "Fernsworth", "Baggins", "Downstrider")

private val menuData = File("data/tavern-menu-data.txt").readText().split("\n")


private val menuDataSplited = List(menuData.size) { menuData[it].split(",") }
private val groupedMenuData = menuDataSplited.groupBy({ it[0] }, { it[1] to it[2] })


private fun placeOrder(patronName: String, menuItemName: String, patronGold: MutableMap<String, Double>) {
    val itemPrice = 1.0
    narrate("$patronName speaks with $TAVERN_MASTER to place an order")
    if (itemPrice <= patronGold.getOrDefault(patronName, 0.0)) {
        narrate("$TAVERN_MASTER hands $patronName a $menuItemName")
        narrate("$patronName pays $TAVERN_MASTER $itemPrice gold")
        patronGold[patronName] = patronGold.getValue(patronName) - itemPrice
        patronGold[TAVERN_MASTER] = patronGold.getValue(TAVERN_MASTER) + itemPrice
    } else {
        narrate("$TAVERN_MASTER says, \"You need more coin for a $menuItemName\"")
    }
}


fun visitTavern() {
    val DOTS_TO_MAX_STRING = 6
    val patrons: MutableSet<String> = mutableSetOf()

    val patronGold = mutableMapOf(
        TAVERN_MASTER to 86.00,
        heroName to 4.50,
        *patrons.map { it to 6.00 }.toTypedArray()
    )

    val maxPair = groupedMenuData.flatMap { it.value }.maxByOrNull { it.first.length + it.second.length }
    val maxPairLengs = (maxPair?.first?.length ?: 0) + (maxPair?.second?.length ?: 0)

    while (patrons.size < 5) {
        val patronName = "${firstNames.random()} ${lastNames.random()}"
        patrons += patronName
    }

    narrate("$heroName enters $TAVERN_NAME")
    narrate("*** Welcome to Taernyl's Folly ***")

    groupedMenuData.forEach { type, pairList ->
        if (maxPairLengs >= type.length)
        println(" ".repeat((maxPairLengs) / 2 - type.length / 2 + DOTS_TO_MAX_STRING / 2) + type)
        else             println(type)

        pairList.forEach { pair ->
            val dots = ".".repeat(maxPairLengs - pair.first.length - pair.second.length + DOTS_TO_MAX_STRING)
            val str = pair.first + dots + pair.second
            println(str)
        }
    }

    narrate("$heroName sees several patrons in the tavern:")
    narrate(patrons.joinToString())
    println(patronGold)
    repeat(10) {
        val randomDish = groupedMenuData.entries.random().value.random().first
        placeOrder(patrons.random(), randomDish, patronGold )
        println(patronGold)
    }

}