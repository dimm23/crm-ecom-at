package ru.sibur.test.crm_ecom_at.web.pages.crm.deal.deal_card

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory

class CrmDealCardPage(
    webdriver: WebDriver
) {
    init {
        PageFactory.initElements(webdriver, this)
    }

    companion object {
        const val DEPARTMENT_INPUT_XPATH = "//div[@rn=\"SIB CRM Quote Detail View\"]//input[@rn=\"Sales Rep Organization Name\"]"
        const val DEPARTMENT_INPUT_LIST_XPATH = "./..//li/span"
        const val QUOTE_LIST_XPATH = "//div[@rn=\"SIB CRM Quote Product Tile Applet\"]//a[@name=\"Product\"]/../../../../.."
        const val QUOTE_LIST_LINK_XPATH = ".//a[@name=\"Product\"]"
        const val QUOTE_LIST_QTY_XPATH = ".//span[@rn=\"Quantity Requested\"]/label"
        const val QUOTE_LIST_PRICE_XPATH = ".//span[@rn=\"Base Price\"]/label"
        const val QUOTE_LIST_SUM_XPATH = ".//span[@rn=\"Total Price\"]/label"
        const val QUOTE_LIST_STATUS_XPATH = ".//span[@rn=\"Status\"]/label"
        const val SAVE_BUTTON_XPATH = "//button[@rn = 'Save']"
        const val PRODUCTS_ROWS_XPATH = "//div[contains(@class, 'sib-crm-tile-row ')]"
        const val OK_BUTTON = "//button[text() = 'OK']"
        const val WARNING_ICON = "//div[contains(@class, 'swal2-warning')]"
        const val CONFIRM_BUTTON = "//button[contains(@class, 'swal2-confirm')]"
        const val FIN_RES_COMMENT = "//textarea[contains(@aria-labelledby, 'FinRes_Comment_Label')]"
        const val DEAL_DATE = "//span[@rn=\"SIB Quote Format Date Calc\"]/label"
        const val DEAL_SOURCE = "//span[@rn=\"SIB CRM Source\"]/label"
        const val SALES_OFFICE = "//input[@rn = 'SIB CRM Sales Office Display']"
        const val SALES_ORG = "//input[@rn = 'SIB CRM Sales Organization Display']"
    }

    @FindBy(xpath = OK_BUTTON)
    lateinit var okButtonsList: MutableList<WebElement>

    @FindBy(xpath = OK_BUTTON)
    lateinit var okButton: WebElement

    @FindBy(xpath = SAVE_BUTTON_XPATH)
    lateinit var saveButton: WebElement

    @FindBy(xpath = PRODUCTS_ROWS_XPATH)
    lateinit var productRows: MutableList<WebElement>

    // Название компании в заголовке
    @FindBy(xpath = "//*[@rn = 'Opportunity #']")
    lateinit var companyNameInTitle: WebElement

    // статус (CRM)
    @FindBy(xpath = "//div[@rn=\"SIB CRM Quote Detail View\"]//div[@class=\"sib-crm-quote-form-applet\"]//*[@rn=\"Status\"]")
    lateinit var statusInput: WebElement

    // инициатор
    @FindBy(xpath = "//div[@rn=\"SIB CRM Quote Detail View\"]//div[@class=\"sib-crm-manager-name\"]//label")
    lateinit var initiatorNameLabel: WebElement

    // должность инициатора
    @FindBy(xpath = "//div[@rn=\"SIB CRM Quote Detail View\"]//div[@class=\"sib-crm-manager-position\"]//label")
    lateinit var initiatorPositionLabel: WebElement

    // картинка инициатора
    @FindBy(xpath = "//div[@rn=\"SIB CRM Quote Detail View\"]//div[@class=\"sib-crm-manager-icon\"]")
    lateinit var initiatorPic: WebElement

    // кнопка команды
    @FindBy(xpath = "//div[@rn=\"SIB CRM Quote Detail View\"]//div[contains(@class,\"sib-crm-client-team-btn\")]//button")
    lateinit var teamBtn: WebElement

    // клиент
    @FindBy(xpath = "//div[@rn=\"SIB CRM Quote Detail View\"]//*[@rn=\"Opportunity #\"]/label")
    lateinit var clientNameLabel: WebElement

    // шапка
    @FindBy(xpath = "//div[@rn=\"SIB CRM Quote Detail View\"]//*[@rn=\"Quote Number\"]/label")
    lateinit var dealNumberLabel: WebElement
    @FindBy(xpath = DEAL_DATE)
    lateinit var dealCreateDateLabel: WebElement
    @FindBy(xpath = DEAL_SOURCE)
    lateinit var dealSourceLabel: WebElement
    @FindBy(xpath = "//a[contains(@href, 'SIB My Quotes List View')]")
    lateinit var backToMyDealsListLink:WebElement

    // кнопки
    @FindBy(xpath = "//div[@rn=\"SIB CRM Quote Detail View\"]//button[@rn=\"CopyQuote\"]")
    lateinit var copyQuoteBtn: WebElement
    @FindBy(xpath = "//div[@rn=\"SIB CRM Quote Detail View\"]//button[@rn=\"SendToSAP\"]")
    lateinit var sendToSapBtn: WebElement
    @FindBy(xpath = "//div[@rn=\"SIB CRM Quote Detail View\"]//button[@rn=\"Create RCM\"]")
    lateinit var createRCMBtn: WebElement
    @FindBy(xpath = "//div[@rn=\"SIB CRM Quote Detail View\"]//button[@rn=\"RejectSAPPopup\"]")
    lateinit var cancelInSapBtn: WebElement
    @FindBy(xpath = "//div[@rn=\"SIB CRM Quote Detail View\"]//button[@rn=\"DeleteDeal\"]")
    lateinit var deleteDealBtn: WebElement

    // клиент
    @FindBy(xpath = "//div[@rn=\"SIB CRM Quote Detail View\"]//input[@rn=\"ClientName\"]")
    lateinit var clientInput: WebElement
    // линка в клиента
    @FindBy(xpath = "//div[@rn=\"SIB CRM Quote Detail View\"]//a[@rn=\"ClientName\"]")
    lateinit var clientFieldLink: WebElement
    // сумма с НДС
    @FindBy(xpath = "//div[@rn=\"SIB CRM Quote Detail View\"]//input[@rn=\"SIB Summary Real\"]")
    lateinit var sumWithVatInput: WebElement
    // дирекция
    @FindBy(xpath = DEPARTMENT_INPUT_XPATH)
    lateinit var directionInput: WebElement
    // валюта
    @FindBy(xpath = "//div[@rn=\"SIB CRM Quote Detail View\"]//input[@rn=\"Currency\"]")
    lateinit var currencySDD: WebElement
    // договор
    @FindBy(xpath = "//div[@rn=\"SIB CRM Quote Detail View\"]//input[@rn=\"AgreementExt\"]")
    lateinit var contractSDD: WebElement
    // канал сбыта
    @FindBy(xpath = "//div[@rn=\"SIB CRM Quote Detail View\"]//input[@rn=\"Sales Pipeline\"]")
    lateinit var distributionChannelSDD: WebElement
    // коммент
    @FindBy(xpath = "//div[@rn=\"SIB CRM Quote Detail View\"]//textarea[@rn=\"Comment\"]")
    lateinit var commentTextarea: WebElement
    // СИ чекбокс
    @FindBy(xpath = "//div[@rn=\"SIB CRM Quote Detail View\"]//input[@rn=\"SIB SI Flg\"]")
    lateinit var siChBInput: WebElement
    // СТЗ чекбокс
    @FindBy(xpath = "//div[@rn=\"SIB CRM Quote Detail View\"]//input[@rn=\"SIB Customs Free Zone Flg\"]")
    lateinit var freeVatZoneChBInput: WebElement
    // Отдел
    @FindBy(xpath = SALES_OFFICE)
    lateinit var salesOfficeSDD: WebElement
    // Сбытовая организация
    @FindBy(xpath = SALES_ORG)
    lateinit var salesOrganizationSDD: WebElement
    // о2с
    @FindBy(xpath = "//div[@rn=\"SIB CRM Quote Detail View\"]//input[@rn=\"SIB O2C Instrument\"]")
    lateinit var o2cSDD: WebElement

    // кнопка +
    @FindBy(xpath = "//div[@rn=\"SIB CRM Quote Detail View\"]//button[@rn=\"NewProcessingRecord\"]")
    lateinit var addQuoteItemBtn: WebElement

    @FindBy(xpath = QUOTE_LIST_XPATH)
    lateinit var quoteList: MutableList<WebElement>

    @FindBy(xpath = "//div[@rn=\"SIB Quote GTM List Applet\"]//span[@rn=\"Agreement Number Calc\"]")
    lateinit var additionalAggreementNumLabel: WebElement
    @FindBy(xpath = "//div[@rn=\"SIB Quote GTM List Applet\"]//span[@rn=\"Name\"]")
    lateinit var gtmNumLabel: WebElement
    @FindBy(xpath = "//div[@rn=\"SIB Quote GTM List Applet\"]//span[@rn=\"SIB RCM\"]")
    lateinit var rcmNumLabel: WebElement
    @FindBy(xpath = QUOTE_LIST_LINK_XPATH)
    lateinit var productLink: MutableList<WebElement>
    @FindBy(xpath = FIN_RES_COMMENT)
    lateinit var finResCommentField: WebElement

    // диалоговое окно - Внимание значение отклоняется от тарифа СВТ
    @FindBy(xpath = WARNING_ICON)
    lateinit var warningIcon: WebElement
    @FindBy(xpath = CONFIRM_BUTTON)
    lateinit var confirmButton: WebElement

}