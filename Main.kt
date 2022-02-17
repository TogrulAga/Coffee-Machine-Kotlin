package machine

import kotlin.system.exitProcess

enum class States {
    CHOOSING_ACTION, SELLING_COFFEE, FILLING_RESOURCES, TAKING_MONEY, PRINTING_RESOURCES
}

enum class Actions {
    BUY, FILL, TAKE, REMAINING, EXIT
}

enum class ResourcesState {
    WATER, MILK, COFFEE_BEANS, DISPOSABLE_CUPS
}

enum class Coffees(val menuOrder: String, val water: Int, val milk: Int, val coffeeBeans : Int, val money: Int) {
    ESPRESSO("1", 250, 0, 16, 4),
    LATTE("2", 350, 75, 20, 7),
    CAPPUCCINO("3", 200, 100, 12, 6),
    BACK("0", 0, 0, 0, 0)
}

object CoffeeMachine {
    private var money: Int = 550
    private var water: Int = 400
    private var milk: Int = 540
    private var coffeeBeans: Int = 120
    private var disposableCups: Int = 9
    private var state: States = States.CHOOSING_ACTION
    private var resourcesState = ResourcesState.WATER

    fun input(command: String) {
        when (state) {
            States.CHOOSING_ACTION -> {
                when (command) {
                    Actions.BUY.name.lowercase() -> state = States.SELLING_COFFEE
                    Actions.FILL.name.lowercase() -> state = States.FILLING_RESOURCES
                    Actions.TAKE.name.lowercase() -> state = States.TAKING_MONEY
                    Actions.REMAINING.name.lowercase() -> state = States.PRINTING_RESOURCES
                    Actions.EXIT.name.lowercase() -> exitProcess(0)
                }
            }
            States.SELLING_COFFEE -> {
                when (command) {
                    Coffees.ESPRESSO.menuOrder -> sellCoffee(Coffees.ESPRESSO)
                    Coffees.LATTE.menuOrder -> sellCoffee(Coffees.LATTE)
                    Coffees.CAPPUCCINO.menuOrder -> sellCoffee(Coffees.CAPPUCCINO)
                    Coffees.BACK.menuOrder -> state = States.CHOOSING_ACTION
                }
                state = States.CHOOSING_ACTION
            }
            States.FILLING_RESOURCES -> {
                when (resourcesState) {
                    ResourcesState.WATER -> {
                        fillWater(command)
                        resourcesState = ResourcesState.MILK
                    }
                    ResourcesState.MILK -> {
                        fillMilk(command)
                        resourcesState = ResourcesState.COFFEE_BEANS
                    }
                    ResourcesState.COFFEE_BEANS -> {
                        fillCoffeeBeans(command)
                        resourcesState = ResourcesState.DISPOSABLE_CUPS
                    }
                    ResourcesState.DISPOSABLE_CUPS -> {
                        fillDisposableCups(command)
                        resourcesState = ResourcesState.WATER
                        state = States.CHOOSING_ACTION
                        println()
                    }
                }
            }
        }
    }

    fun printMenu() {
        when (state) {
            States.CHOOSING_ACTION -> print("Write action (buy, fill, take, remaining, exit): ")
            States.SELLING_COFFEE -> print(
                "\nWhat do you want to buy? 1 - espresso, 2 - latte," +
                        " 3 - cappuccino, back - to main menu: "
            )
            States.FILLING_RESOURCES -> {
                when (resourcesState) {
                    ResourcesState.WATER -> print("\nWrite how many ml of water do you want to add: ")
                    ResourcesState.MILK -> print("Write how many ml of milk do you want to add: ")
                    ResourcesState.COFFEE_BEANS -> print("Write how many disposable cups of coffee do you want to add: ")
                    ResourcesState.DISPOSABLE_CUPS -> print("Write how many grams of coffee beans do you want to add: ")
                }
            }
            States.TAKING_MONEY -> {
                takeMoney()
                state = States.CHOOSING_ACTION
                printMenu()
            }
            States.PRINTING_RESOURCES -> {
                printResources()
                state = States.CHOOSING_ACTION
                printMenu()
            }
        }
    }

    private fun fillDisposableCups(command: String) {
        disposableCups += command.toInt()
    }

    private fun fillCoffeeBeans(command: String) {
        coffeeBeans += command.toInt()
    }

    private fun fillMilk(command: String) {
        milk += command.toInt()
    }

    private fun fillWater(command: String) {
        water += command.toInt()
    }

    private fun printResources() {
        println("\nThe coffee machine has:")
        println("$water ml of water")
        println("$milk ml of milk")
        println("$coffeeBeans g of coffee beans")
        println("$disposableCups disposable cups")
        println("\$$money of money\n")
    }

    private fun sellCoffee(coffees: Coffees) {
        if (water < coffees.water) {
            println("Sorry, not enough water!\n")
        } else if (milk < coffees.milk) {
            println("Sorry, not enough milk!\n")
        } else if (coffeeBeans < coffees.coffeeBeans) {
            println("Sorry, not enough coffee beans!\n")
        } else if (disposableCups < 1) {
            println("Sorry, not enough disposable cups!\n")
        } else {
            println("I have enough resources, making you a coffee!\n")
            water -= coffees.water
            milk -= coffees.milk
            coffeeBeans -= coffees.coffeeBeans
            disposableCups -= 1
            money += coffees.money
        }
    }

    private fun takeMoney() {
        println("\nI gave you \$$money\n")
        money = 0
    }
}

fun main() {
    while (true) {
        CoffeeMachine.printMenu()
        CoffeeMachine.input(readln())
    }
}
