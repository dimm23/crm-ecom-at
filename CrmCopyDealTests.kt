package ru.sibur.test.crm_ecom_at.tests.crm.deals

import io.qameta.allure.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import org.springframework.beans.factory.annotation.Autowired
import ru.sibur.test.crm_ecom_at.common.model.TestConsts
import ru.sibur.test.crm_ecom_at.common.model.crm.deal.CrmDealUiModel
import ru.sibur.test.crm_ecom_at.common.model.crm.deal.QuoteCommandInfoUiModel
import ru.sibur.test.crm_ecom_at.common.model.crm.deal.QuoteUiModel
import ru.sibur.test.crm_ecom_at.common.model.enums.crm.CrmMainMenuItems
import ru.sibur.test.crm_ecom_at.tests.argument_providers.CrmEcomArgProviders
import ru.sibur.test.crm_ecom_at.tests.base.WebTestBase
import ru.sibur.test.crm_ecom_at.tests.context.IntegrationTestContext
import ru.sibur.test.crm_ecom_at.tests.extensions.LogOutCrmExtension
import ru.sibur.test.crm_ecom_at.tests.steps.db.integration.IntegrationDealSteps
import ru.sibur.test.crm_ecom_at.tests.steps.web.BaseWebSteps
import ru.sibur.test.crm_ecom_at.tests.steps.web.crm.*
import ru.sibur.test.crm_ecom_at.tests.steps.web.crm.dealCard.CrmDealCardSteps
import ru.sibur.test.crm_ecom_at.tests.steps.web.crm.dealCard.CrmQuoteItemCardSteps
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Epic(value = "CRM")
@Feature(value = "CRM \\ Deals")
@Story(value = "Копирование сделки")
@Tags(value = [Tag("crm"), Tag("deal"), Tag("web")])
@ExtendWith(LogOutCrmExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CrmCopyDealTests: WebTestBase() {
    @Autowired
    private lateinit var baseWebSteps: BaseWebSteps
    @Autowired
    private lateinit var integrationTestContext: IntegrationTestContext
    @Autowired
    lateinit var crmLoginSteps: CrmLoginSteps
    @Autowired
    lateinit var crmMainSteps: CrmMainSteps
    @Autowired
    lateinit var crmDealsSteps: CrmDealsSteps
    @Autowired
    lateinit var crmDealCardSteps: CrmDealCardSteps
    @Autowired
    lateinit var crmRecapSteps: CrmRecapsSteps
    @Autowired
    lateinit var crmRecapCardSteps: CrmRecapCardSteps
    @Autowired
    lateinit var crmQuoteItemCardSteps: CrmQuoteItemCardSteps
    @Autowired
    lateinit var integrationDealSteps: IntegrationDealSteps


    @ParameterizedTest(name = "Отправка скопированной сделки СХ в САП Part 1")
    @ArgumentsSource(CrmEcomArgProviders.DealsDataListOfCxGtmNums::class)
    @Owner("osipovdma")
    @Tags(Tag("t4"), Tag("cxDealsRegress1"))
    @AllureId("6409")
    fun testCopyCxDeals(gtm: String, fm: String, jtHead: String, jtBack: String) {
        Allure.parameter("Копируем сделку с  GTM номером: ", gtm)
        integrationTestContext.store("locale", TestConsts.RU)

        val url = selectUrl(TestConsts.RU)
        crmLoginSteps.login(
            url,
            commonProperties.newCrm.username,
            commonProperties.newCrm.password
        )

        crmMainSteps.openScreen(item = CrmMainMenuItems.Deals)
        crmMainSteps.waitTillScreenLoaded(item = CrmMainMenuItems.Deals)
        crmDealsSteps.searchDealByGtmNumber(gtm)
        crmDealCardSteps.rememberDeal("SourceDeal")

        crmDealCardSteps.copyDeal()
        val SourceDeal = integrationTestContext.fetch("SourceDeal", clazz = CrmDealUiModel::class)
        val updateDeal = CrmDealUiModel()
        updateDeal.siFlg = false
        updateDeal.contract = integrationTestContext.fetch("SourceDeal", clazz = CrmDealUiModel::class).contract
        updateDeal.products.add(QuoteUiModel())
        updateDeal.products[0].generalInfoTabUiModel.additionalAgreementDate = SimpleDateFormat("dd.MM.yyyy").format(Date())
        updateDeal.products[0].deliveryTabUiModel.warehouse = SourceDeal.products[0].deliveryTabUiModel.warehouse
        updateDeal.products[0].deliveryTabUiModel.shippingPlantList.add(SourceDeal.products[0].deliveryTabUiModel.shippingPlantList[0])
        updateDeal.products[0].deliveryTabUiModel.destination =
            SourceDeal.products[0].deliveryTabUiModel.destination?.split(" ")?.get(0)
        updateDeal.products[0].pricingTabUiModel.premiumOrDiscountList = SourceDeal.products[0].pricingTabUiModel.premiumOrDiscountList
        updateDeal.products[0].pricingTabUiModel.logistic = SourceDeal.products[0].pricingTabUiModel.logistic
        updateDeal.products[0].pricingTabUiModel.logisticToClient = SourceDeal.products[0].pricingTabUiModel.logisticToClient
        updateDeal.products[0].pricingTabUiModel.logisticToMOX = SourceDeal.products[0].pricingTabUiModel.logisticToMOX
        updateDeal.products[0].pricingTabUiModel.deliveryIncluded = SourceDeal.products[0].pricingTabUiModel.deliveryIncluded

        updateDeal.products[0].commandInfoUiModel.fm = QuoteCommandInfoUiModel.Member(name = fm)
        updateDeal.products[0].commandInfoUiModel.jtHead = QuoteCommandInfoUiModel.Member(name = jtHead)
        updateDeal.products[0].commandInfoUiModel.jtBack = QuoteCommandInfoUiModel.Member(name = jtBack)

        integrationTestContext.store(item = updateDeal)
        crmDealCardSteps.fillDealFieldsFromContext()

        integrationTestContext.store("openDealPositionIndex", 0 as Int)
        crmDealCardSteps.openDealPosition()

        crmQuoteItemCardSteps.setGeneralInfoFields()
        crmQuoteItemCardSteps.setQuoteItemTeam()
        crmQuoteItemCardSteps.setDeliveryFields()
        crmQuoteItemCardSteps.setPricingFields()

        crmQuoteItemCardSteps.rememberDealNumber()
        val dealNumber = integrationTestContext.fetch("DealNumber", clazz = String::class)

        crmQuoteItemCardSteps.sendToSap()

        crmMainSteps.openScreen(item = CrmMainMenuItems.Deals)
        crmMainSteps.waitTillScreenLoaded(item = CrmMainMenuItems.Deals)
        integrationTestContext.store("dealNumber", dealNumber)
        crmDealsSteps.openDealByDealNumber()

        // Создаём ДС для сделки
        crmDealCardSteps.addNewDs()
        baseWebSteps.refreshPage()

        // Проверяем что появился RCM номер
        crmQuoteItemCardSteps.openProductInDeal()
        crmDealsSteps.checkRcmNumber()
        crmQuoteItemCardSteps.goToTheDealFromProduct()

        crmDealCardSteps.rememberDeal("CopiedDeal")
        integrationTestContext.store(item = integrationTestContext.fetch("CopiedDeal", clazz = CrmDealUiModel::class))

        crmDealCardSteps.compareCopiedDealVsSource()
        crmLoginSteps.logoff()
        integrationDealSteps.validateGtmMsgs()

        Allure.step("Проверка тегов: ", Allure.ThrowableRunnable {
            baseWebSteps.softlyAssertAll()
        })

    }

    @ParameterizedTest(name = "Отправка скопированной сделки СХ в САП Part 2")
    @ArgumentsSource(CrmEcomArgProviders.DealsDataListOfCxGtmNumsPart2::class)
    @Owner("osipovdma")
    @Tags(Tag("t4"), Tag("cxDealsRegress2"))
    @AllureId("22036")
    fun testCopyCxDealsPart2(gtm: String, fm: String, jtHead: String, jtBack: String) {
        Allure.parameter("Копируем сделку с  GTM номером: ", gtm)
        integrationTestContext.store("locale", TestConsts.RU)

        val url = selectUrl(TestConsts.RU)
        crmLoginSteps.login(
            url,
            commonProperties.newCrm.username,
            commonProperties.newCrm.password
        )

        crmMainSteps.openScreen(item = CrmMainMenuItems.Deals)
        crmMainSteps.waitTillScreenLoaded(item = CrmMainMenuItems.Deals)
        crmDealsSteps.searchDealByGtmNumber(gtm)
        crmDealCardSteps.rememberDeal("SourceDeal")

        crmDealCardSteps.copyDeal()
        val SourceDeal = integrationTestContext.fetch("SourceDeal", clazz = CrmDealUiModel::class)
        val updateDeal = CrmDealUiModel()
        updateDeal.siFlg = false
        updateDeal.contract = integrationTestContext.fetch("SourceDeal", clazz = CrmDealUiModel::class).contract
        updateDeal.products.add(QuoteUiModel())
        updateDeal.products[0].generalInfoTabUiModel.additionalAgreementDate = SimpleDateFormat("dd.MM.yyyy").format(Date())
        updateDeal.products[0].deliveryTabUiModel.destination =
            SourceDeal.products[0].deliveryTabUiModel.destination?.split(" ")?.get(0)
        updateDeal.products[0].deliveryTabUiModel.warehouse = SourceDeal.products[0].deliveryTabUiModel.warehouse
        updateDeal.products[0].deliveryTabUiModel.shippingPlantList.add(SourceDeal.products[0].deliveryTabUiModel.shippingPlantList[0])
        updateDeal.products[0].pricingTabUiModel.premiumOrDiscountList = SourceDeal.products[0].pricingTabUiModel.premiumOrDiscountList
        updateDeal.products[0].pricingTabUiModel.logistic = SourceDeal.products[0].pricingTabUiModel.logistic
        updateDeal.products[0].pricingTabUiModel.logisticToClient = SourceDeal.products[0].pricingTabUiModel.logisticToClient
        updateDeal.products[0].pricingTabUiModel.logisticToMOX = SourceDeal.products[0].pricingTabUiModel.logisticToMOX
        updateDeal.products[0].pricingTabUiModel.deliveryIncluded = SourceDeal.products[0].pricingTabUiModel.deliveryIncluded

        updateDeal.products[0].commandInfoUiModel.fm = QuoteCommandInfoUiModel.Member(name = fm)
        updateDeal.products[0].commandInfoUiModel.jtHead = QuoteCommandInfoUiModel.Member(name = jtHead)
        updateDeal.products[0].commandInfoUiModel.jtBack = QuoteCommandInfoUiModel.Member(name = jtBack)

        integrationTestContext.store(item = updateDeal)
        crmDealCardSteps.fillDealFieldsFromContext()

        integrationTestContext.store("openDealPositionIndex", 0 as Int)
        crmDealCardSteps.openDealPosition()

        crmQuoteItemCardSteps.setGeneralInfoFields()
        crmQuoteItemCardSteps.setQuoteItemTeam()
        crmQuoteItemCardSteps.setDeliveryFields()
        crmQuoteItemCardSteps.setPricingFields()

        crmQuoteItemCardSteps.rememberDealNumber()
        val dealNumber = integrationTestContext.fetch("DealNumber", clazz = String::class)

        crmQuoteItemCardSteps.sendToSap()

        crmMainSteps.openScreen(item = CrmMainMenuItems.Deals)
        crmMainSteps.waitTillScreenLoaded(item = CrmMainMenuItems.Deals)
        integrationTestContext.store("dealNumber", dealNumber)
        crmDealsSteps.openDealByDealNumber()

        // Создаём ДС для сделки
        crmDealCardSteps.addNewDs()
        baseWebSteps.refreshPage()

        // Проверяем что появился RCM номер
        crmQuoteItemCardSteps.openProductInDeal()
        crmDealsSteps.checkRcmNumber()
        crmQuoteItemCardSteps.goToTheDealFromProduct()

        crmDealCardSteps.rememberDeal("CopiedDeal")
        integrationTestContext.store(item = integrationTestContext.fetch("CopiedDeal", clazz = CrmDealUiModel::class))

        crmDealCardSteps.compareCopiedDealVsSource()
        crmLoginSteps.logoff()
        integrationDealSteps.validateGtmMsgs()

        Allure.step("Проверка тегов: ", Allure.ThrowableRunnable {
            baseWebSteps.softlyAssertAll()
        })
    }

    @ParameterizedTest(name = "Отправка сделки из рекапа SI Vienna в САП")
    @ArgumentsSource(CrmEcomArgProviders.SiViennaGtmNums::class)
    @Owner("osipovdma")
    @Tags(Tag("t4"), Tag("siDealsRegress"))
    @AllureId("21467")
    fun testCopySiDeals(gtm: String, fm: String, jtHead: String, jtBack: String) {
        Allure.parameter("Копируем рекап, найденный по gtm номеру сделки: ", gtm)
        integrationTestContext.store("locale", TestConsts.EN)

        // Выполняем вход под КМ (FM)
        val url = selectUrl(TestConsts.EN)
        crmLoginSteps.login(
            url,
            commonProperties.newCrm.usernameNebotov,
            commonProperties.newCrm.passwordNebotov
        )
        // Переходим на экран сделки
        crmMainSteps.openScreen(item = CrmMainMenuItems.Deals)
        crmMainSteps.waitTillScreenLoaded(item = CrmMainMenuItems.Deals)
        // Находим сделку по gtm номеру и запоминаем её параметры
        crmDealsSteps.searchDealByGtmNumber(gtm)
        crmDealCardSteps.rememberDeal("SourceDeal")
        val SourceDeal = integrationTestContext.fetch("SourceDeal", clazz = CrmDealUiModel::class)
        val updateDeal = CrmDealUiModel()
        updateDeal.products.add(QuoteUiModel())
        updateDeal.products[0].deliveryTabUiModel.customsStatus = SourceDeal.products[0].deliveryTabUiModel.customsStatus
        updateDeal.products[0].deliveryTabUiModel.warehouse = SourceDeal.products[0].deliveryTabUiModel.warehouse
        updateDeal.products[0].pricingTabUiModel.premiumOrDiscountList = SourceDeal.products[0].pricingTabUiModel.premiumOrDiscountList

        integrationTestContext.store("updateDeal", updateDeal)
        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()

        crmMainSteps.openScreen(item = CrmMainMenuItems.Recaps)
        crmMainSteps.waitTillScreenLoaded(item = CrmMainMenuItems.Recaps)
        // Находим рекап по номеру
        crmRecapSteps.searchRecapByNumber(SourceDeal.dealNumber.toString())
        // Копируем найденный рекап
        crmRecapCardSteps.copyRecap()
        crmRecapCardSteps.openProductPositionInRecap()
        crmRecapCardSteps.setDeliveryFields("updateDeal")
        crmRecapCardSteps.setPricingFields("updateDeal")
        crmRecapCardSteps.goToTheRecapFromProduct()

        // Отправляем рекап в ЦКС
        crmRecapCardSteps.sendToSCS()
        crmRecapCardSteps.rememberRecapNumber()
        crmLoginSteps.logoff()

        // Выполняем вход под JT Back
        crmLoginSteps.login(
            url,
            commonProperties.newCrm.userJTBack,
            commonProperties.newCrm.passwordJTBack
        )
        crmMainSteps.openScreen(item = CrmMainMenuItems.Deals)
        crmMainSteps.waitTillScreenLoaded(item = CrmMainMenuItems.Deals)

        crmDealsSteps.searchDealByNumber(integrationTestContext.fetch("recapNumber", clazz = String::class))
        crmRecapCardSteps.takeDealFromRecapInProcess()

        updateDeal.siFlg = true
        updateDeal.contract = SourceDeal.contract
        updateDeal.products[0].generalInfoTabUiModel.additionalAgreementDate = SimpleDateFormat("MM/dd/yyyy").format(Date())
        updateDeal.products[0].generalInfoTabUiModel.salesDocType = SourceDeal.products[0].generalInfoTabUiModel.salesDocType
        updateDeal.products[0].generalInfoTabUiModel.salesType = SourceDeal.products[0].generalInfoTabUiModel.salesType
        updateDeal.products[0].deliveryTabUiModel.shipper = SourceDeal.products[0].deliveryTabUiModel.shipper
        updateDeal.products[0].deliveryTabUiModel.shippingPlant = SourceDeal.products[0].deliveryTabUiModel.shippingPlant
        updateDeal.products[0].deliveryTabUiModel.destination =
            SourceDeal.products[0].deliveryTabUiModel.destination?.split(" ")?.get(0)
        updateDeal.products[0].pricingTabUiModel.premiumOrDiscountList.clear()
        updateDeal.products[0].commandInfoUiModel.fm = QuoteCommandInfoUiModel.Member(name = fm)
        updateDeal.products[0].commandInfoUiModel.jtHead = QuoteCommandInfoUiModel.Member(name = jtHead)
        updateDeal.products[0].commandInfoUiModel.jtBack = QuoteCommandInfoUiModel.Member(name = jtBack)

        integrationTestContext.store(item = updateDeal)
        // JT дозаполняет сделку из рекапа
        crmDealCardSteps.fillDealFieldsFromContext()

        integrationTestContext.store("openDealPositionIndex", 0 as Int)
        crmDealCardSteps.openDealPosition()

        crmQuoteItemCardSteps.setGeneralInfoFields()
        crmQuoteItemCardSteps.setQuoteItemTeam()
        crmQuoteItemCardSteps.setDeliveryFields()
        crmQuoteItemCardSteps.setPricingFields()

        crmQuoteItemCardSteps.rememberDealNumber()
        val dealNumber = integrationTestContext.fetch("DealNumber", clazz = String::class)

        crmQuoteItemCardSteps.sendToSap()

        crmMainSteps.openScreen(item = CrmMainMenuItems.Deals)
        crmMainSteps.waitTillScreenLoaded(item = CrmMainMenuItems.Deals)
        integrationTestContext.store("dealNumber", dealNumber)
        crmDealsSteps.openDealByDealNumber()

        // Создаём ДС для сделки
        crmDealCardSteps.addNewDs()
        baseWebSteps.refreshPage()

        // Проверяем что появился RCM номер
        crmQuoteItemCardSteps.openProductInDeal()
        crmDealsSteps.checkRcmNumber()
        crmQuoteItemCardSteps.goToTheDealFromProduct()

        crmDealCardSteps.rememberDeal("CopiedDeal")
        integrationTestContext.store(item = integrationTestContext.fetch("CopiedDeal", clazz = CrmDealUiModel::class))

        crmDealCardSteps.compareCopiedDealVsSource()
        crmLoginSteps.logoff()
        integrationDealSteps.validateGtmMsgs()

        Allure.step("Проверка тегов: ", Allure.ThrowableRunnable {
            baseWebSteps.softlyAssertAll()
        })
    }
    @ParameterizedTest(name = "Отправка скопированной сделки SI Shanghai в САП")
    @ArgumentsSource(CrmEcomArgProviders.SiShanghaiGtmNums::class)
    @Owner("osipovdma")
    @Tags(Tag("t4"), Tag("siDealsRegress"))
    @AllureId("21541")
    fun testCopySiShanghaiDeals(gtm: String, fm: String, jtHead: String, jtBack: String) {
        Allure.parameter("Копируем сделку с  GTM номером: ", gtm)
        integrationTestContext.store("locale", TestConsts.RU)

        val url = selectUrl(TestConsts.RU)
        crmLoginSteps.login(
            url,
            commonProperties.newCrm.username,
            commonProperties.newCrm.password
        )

        crmMainSteps.openScreen(item = CrmMainMenuItems.Deals)
        crmMainSteps.waitTillScreenLoaded(item = CrmMainMenuItems.Deals)
        crmDealsSteps.searchDealByGtmNumber(gtm)
        crmDealCardSteps.rememberDeal("SourceDeal")

        crmDealCardSteps.copyDeal()
        val SourceDeal = integrationTestContext.fetch("SourceDeal", clazz = CrmDealUiModel::class)
        val updateDeal = CrmDealUiModel()
        updateDeal.siFlg = true
        updateDeal.contract = SourceDeal.contract
        updateDeal.products.add(QuoteUiModel())
        updateDeal.products[0].generalInfoTabUiModel.additionalAgreementDate = SimpleDateFormat("dd.MM.yyyy").format(Date())
        updateDeal.products[0].pricingTabUiModel.premiumOrDiscountList = SourceDeal.products[0].pricingTabUiModel.premiumOrDiscountList
        updateDeal.products[0].pricingTabUiModel.manualPrice =  SourceDeal.products[0].pricingTabUiModel.manualPrice
        updateDeal.products[0].generalInfoTabUiModel.salesDocType = SourceDeal.products[0].generalInfoTabUiModel.salesDocType
        updateDeal.products[0].deliveryTabUiModel.customsStatus = SourceDeal.products[0].deliveryTabUiModel.customsStatus
        updateDeal.products[0].deliveryTabUiModel.destination =
            SourceDeal.products[0].deliveryTabUiModel.destination?.split(" ")?.get(2)
        updateDeal.products[0].deliveryTabUiModel.warehouse = SourceDeal.products[0].deliveryTabUiModel.warehouse
        updateDeal.products[0].deliveryTabUiModel.shippingPlant = SourceDeal.products[0].deliveryTabUiModel.shippingPlant

        updateDeal.products[0].commandInfoUiModel.fm = QuoteCommandInfoUiModel.Member(name = fm)
        updateDeal.products[0].commandInfoUiModel.jtHead = QuoteCommandInfoUiModel.Member(name = jtHead)
        updateDeal.products[0].commandInfoUiModel.jtBack = QuoteCommandInfoUiModel.Member(name = jtBack)

        integrationTestContext.store(item = updateDeal)
        crmDealCardSteps.fillDealFieldsFromContext()

        integrationTestContext.store("openDealPositionIndex", 0 as Int)
        crmDealCardSteps.openDealPosition()

        crmQuoteItemCardSteps.setGeneralInfoFields()
        crmQuoteItemCardSteps.setQuoteItemTeam()
        crmQuoteItemCardSteps.setDeliveryFields()
        crmQuoteItemCardSteps.setPricingFields()

        crmQuoteItemCardSteps.rememberDealNumber()
        val dealNumber = integrationTestContext.fetch("DealNumber", clazz = String::class)

        crmQuoteItemCardSteps.sendToSap()

        crmMainSteps.openScreen(item = CrmMainMenuItems.Deals)
        crmMainSteps.waitTillScreenLoaded(item = CrmMainMenuItems.Deals)
        integrationTestContext.store("dealNumber", dealNumber)
        crmDealsSteps.openDealByDealNumber()

        // Создаём ДС для сделки
        crmDealCardSteps.addNewDs()
        baseWebSteps.refreshPage()

        // Проверяем что появился RCM номер
        crmQuoteItemCardSteps.openProductInDeal()
        crmDealsSteps.checkRcmNumber()
        crmQuoteItemCardSteps.goToTheDealFromProduct()

        crmDealCardSteps.rememberDeal("CopiedDeal")
        integrationTestContext.store(item = integrationTestContext.fetch("CopiedDeal", clazz = CrmDealUiModel::class))

        crmDealCardSteps.compareCopiedDealVsSource()
        crmLoginSteps.logoff()
        integrationDealSteps.validateGtmMsgs()

        Allure.step("Проверка тегов: ", Allure.ThrowableRunnable {
            baseWebSteps.softlyAssertAll()
        })
    }
    @ParameterizedTest(name = "Отправка скопированной сделки SI Turkey в САП")
    @ArgumentsSource(CrmEcomArgProviders.SiTurkeyGtmNums::class)
    @Owner("osipovdma")
    @Tags(Tag("t4"), Tag("siDealsRegress"))
    @AllureId("21542")
    fun testCopySiTurkeyDeals(gtm: String, fm: String, jtHead: String, jtBack: String) {
        Allure.parameter("Копируем сделку с  GTM номером: ", gtm)
        integrationTestContext.store("locale", TestConsts.RU)

        val url = selectUrl(TestConsts.RU)
        crmLoginSteps.login(
            url,
            commonProperties.newCrm.username,
            commonProperties.newCrm.password
        )

        crmMainSteps.openScreen(item = CrmMainMenuItems.Deals)
        crmMainSteps.waitTillScreenLoaded(item = CrmMainMenuItems.Deals)
        crmDealsSteps.searchDealByGtmNumber(gtm)
        crmDealCardSteps.rememberDeal("SourceDeal")

        crmDealCardSteps.copyDeal()
        val SourceDeal = integrationTestContext.fetch("SourceDeal", clazz = CrmDealUiModel::class)
        val updateDeal = CrmDealUiModel()
        updateDeal.siFlg = true
        updateDeal.contract = SourceDeal.contract
        updateDeal.products.add(QuoteUiModel())
        updateDeal.products[0].generalInfoTabUiModel.additionalAgreementDate = SimpleDateFormat("dd.MM.yyyy").format(Date())
        updateDeal.products[0].generalInfoTabUiModel.salesDocType = SourceDeal.products[0].generalInfoTabUiModel.salesDocType

        updateDeal.products[0].deliveryTabUiModel.customsStatus = SourceDeal.products[0].deliveryTabUiModel.customsStatus
        updateDeal.products[0].deliveryTabUiModel.destination =
            SourceDeal.products[0].deliveryTabUiModel.destination?.split(" ")?.get(0)
        updateDeal.products[0].deliveryTabUiModel.warehouse = SourceDeal.products[0].deliveryTabUiModel.warehouse
        updateDeal.products[0].deliveryTabUiModel.shippingPlant = SourceDeal.products[0].deliveryTabUiModel.shippingPlant
        updateDeal.products[0].deliveryTabUiModel.deliveryFrom = LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        updateDeal.products[0].deliveryTabUiModel.deliveryTo = LocalDate.now().plusMonths(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

        updateDeal.products[0].pricingTabUiModel.premiumOrDiscountList = SourceDeal.products[0].pricingTabUiModel.premiumOrDiscountList
        updateDeal.products[0].pricingTabUiModel.priceListPrice =  SourceDeal.products[0].pricingTabUiModel.priceListPrice

        updateDeal.products[0].commandInfoUiModel.fm = QuoteCommandInfoUiModel.Member(name = fm)
        updateDeal.products[0].commandInfoUiModel.jtHead = QuoteCommandInfoUiModel.Member(name = jtHead)
        updateDeal.products[0].commandInfoUiModel.jtBack = QuoteCommandInfoUiModel.Member(name = jtBack)

        integrationTestContext.store(item = updateDeal)
        crmDealCardSteps.fillDealFieldsFromContext()

        integrationTestContext.store("openDealPositionIndex", 0 as Int)
        crmDealCardSteps.openDealPosition()

        crmQuoteItemCardSteps.setGeneralInfoFields()
        crmQuoteItemCardSteps.setQuoteItemTeam()
        crmQuoteItemCardSteps.setDeliveryFields()
        crmQuoteItemCardSteps.setPricingFields()

        crmQuoteItemCardSteps.rememberDealNumber()
        val dealNumber = integrationTestContext.fetch("DealNumber", clazz = String::class)

        crmQuoteItemCardSteps.sendToSap()

        crmMainSteps.openScreen(item = CrmMainMenuItems.Deals)
        crmMainSteps.waitTillScreenLoaded(item = CrmMainMenuItems.Deals)
        integrationTestContext.store("dealNumber", dealNumber)
        crmDealsSteps.openDealByDealNumber()

        // Создаём ДС для сделки
        crmDealCardSteps.addNewDs()
        baseWebSteps.refreshPage()

        // Проверяем что появился RCM номер
        crmQuoteItemCardSteps.openProductInDeal()
        crmDealsSteps.checkRcmNumber()
        crmQuoteItemCardSteps.goToTheDealFromProduct()

        crmDealCardSteps.rememberDeal("CopiedDeal")
        integrationTestContext.store(item = integrationTestContext.fetch("CopiedDeal", clazz = CrmDealUiModel::class))

        crmDealCardSteps.compareCopiedDealVsSource()
        crmLoginSteps.logoff()
        integrationDealSteps.validateGtmMsgs()

        Allure.step("Проверка тегов: ", Allure.ThrowableRunnable {
            baseWebSteps.softlyAssertAll()
        })
    }

}