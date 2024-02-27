import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.text.NumberFormat
import java.util.*

/**
 * @author abbybrown
 * @date 02/26/24
 * @filename IncomeTaxCalculator.kt
 *
 *      This file is the front end for developing a federal income tax calculator. The user is able to enter their
 *      numeric salary and receive the amount of federal tax that they owe, along with their net pay.
 *
 * @source https://zetcode.com/kotlin/swing/
 * @source https://www.nerdwallet.com/article/taxes/federal-income-tax-brackets
 * @source https://stackoverflow.com/questions/45592109/how-can-i-convert-numbers-to-currency-format-in-android
 * @source https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/list-of.html
 * @source ChatGPT
 */

class TaxCalculatorApp : JFrame("Tax Calculator") {

    private val incomeTaxCalculator = Taxation()

    private val incomeLabel = JLabel("Income:")
    private val incomeTextField = JTextField(10)

    private val maritalStatusLabel = JLabel("Marital Status:")
    private val maritalStatusComboBox = JComboBox(Taxation.MaritalStatus.values())

    private val calculateButton = JButton("Calculate")
    private val resultLabel = JLabel()
    private val netLabel = JLabel()

    // currency formatter is US currency with max 2 decimal places
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 2
    }

    init {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        layout = GridBagLayout()

        // helps to pad all elements
        val gbc = GridBagConstraints().apply {
            insets = Insets(5, 5, 5, 5)
            fill = GridBagConstraints.HORIZONTAL
            gridwidth = 1
            weightx = 0.5
        }

        addComponent(incomeLabel, gbc, 0, 0)
        addComponent(incomeTextField, gbc, 1, 0)
        addComponent(maritalStatusLabel, gbc, 0, 1)
        addComponent(maritalStatusComboBox, gbc, 1, 1)
        addComponent(calculateButton, gbc, 0, 2, 2, 1)
        addComponent(resultLabel, gbc, 0, 3, 2, 1)
        addComponent(netLabel, gbc, 0, 4, 2, 1)

        calculateButton.addActionListener { calculateTax() }

        pack()
        setLocationRelativeTo(null)
        size = Dimension(350, 200)
        isVisible = true
        isResizable = false
    }

    /**
     * @param component - the java swing component to add
     * @param gbc - the constraints to apply
     * @param x - the column
     * @param y - the row
     * @param width - number of columns to span; default = 1
     * @param height - number of rows to span; default = 1
     *
     *      Function makes adding JComponents easier
     */
    private fun addComponent(component: JComponent, gbc: GridBagConstraints, x: Int, y: Int, width: Int = 1, height: Int = 1) {
        gbc.gridx = x
        gbc.gridy = y
        gbc.gridwidth = width
        gbc.gridheight = height
        add(component, gbc)
    }

    /**
     * This function takes no parameters. It calls the calculateIncomeTax function from Taxation.kt.
     *
     * Soft Requirements:
     *      - A label within the GUI for tax taken
     *      - A lebel within the GUI for net pay
     *      - An income text field for user salary
     *
     * Each of these items will be adjusted once calculation proceeds. Function is bound to the calculate button.
     */
    private fun calculateTax() {
        val incomeText = incomeTextField.text
        if (incomeText.isBlank()) {
            resultLabel.text = "Please enter a valid income."
            return
        }

        val income = incomeText.toDoubleOrNull()
        if (income == null) {
            resultLabel.text = "Invalid income amount."
            return
        }

        val maritalStatus = maritalStatusComboBox.selectedItem as Taxation.MaritalStatus
        val (tax, net) = incomeTaxCalculator.calculateIncomeTax(income, maritalStatus)
        resultLabel.text = "Income Tax: ${currencyFormat.format(tax)}"
        netLabel.text = "Take Home Pay: ${currencyFormat.format(net)}"
    }

    // a weird and fancy way to do main... but Java Swing version
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SwingUtilities.invokeLater { TaxCalculatorApp() }
        }
    }
}
