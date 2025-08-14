package ru.sibur.test.crm_ecom_at.tests.crm.deals

import io.qameta.allure.*
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import org.springframework.beans.factory.annotation.Autowired
import ru.sibur.test.crm_ecom_at.common.annotations.Manual
import ru.sibur.test.crm_ecom_at.common.model.AliasConsts
import ru.sibur.test.crm_ecom_at.common.model.TestConsts
import ru.sibur.test.crm_ecom_at.common.model.crm.deal.CrmDealUiModel
import ru.sibur.test.crm_ecom_at.common.model.crm.deal.QuoteCommandInfoUiModel
import ru.sibur.test.crm_ecom_at.common.model.crm.deal.QuoteUiModel
import ru.sibur.test.crm_ecom_at.common.model.enums.crm.CrmMainMenuItems
import ru.sibur.test.crm_ecom_at.common.model.enums.crm.deal.CrmDealStatus
import ru.sibur.test.crm_ecom_at.common.utlis.CsvTestsParameterFiller
import ru.sibur.test.crm_ecom_at.tests.argument_providers.CrmEcomArgProviders
import ru.sibur.test.crm_ecom_at.tests.base.WebTestBase
import ru.sibur.test.crm_ecom_at.tests.context.IntegrationTestContext
import ru.sibur.test.crm_ecom_at.tests.steps.web.BaseWebSteps
import ru.sibur.test.crm_ecom_at.tests.steps.web.crm.CrmDealsSteps
import ru.sibur.test.crm_ecom_at.tests.steps.web.crm.CrmLoginSteps
import ru.sibur.test.crm_ecom_at.tests.steps.web.crm.CrmMainSteps
import ru.sibur.test.crm_ecom_at.tests.steps.web.crm.dealCard.CrmDealCardSteps
import ru.sibur.test.crm_ecom_at.tests.steps.web.crm.dealCard.CrmQuoteItemCardSteps
import java.text.SimpleDateFormat
import java.util.*

@Epic(value = "CRM")
@Feature(value = "CRM \\ Deals")
@Tags(value = [Tag("crm"), Tag("deal"), Tag("web")])
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CrmCxDealsTests: WebTestBase() {
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
    lateinit var crmQuoteItemCardSteps: CrmQuoteItemCardSteps
    @Autowired
    lateinit var csvTestsParameterFiller: CsvTestsParameterFiller
    @Autowired
    private lateinit var baseWebSteps: BaseWebSteps


    @ParameterizedTest(name = "Создание сделки CX и успешная отправка в САП")
    @ArgumentsSource(CrmEcomArgProviders.DealsDataCxTerm::class)
    @Owner("osipovdma")
    @Tags(value = [Tag("regress"), Tag("run")])
    @AllureId("3824")
    fun testCreateCxDealAndSendToSap(locale: String, dataId: String) {
        Allure.parameter(AliasConsts.LOCALE, locale)
        integrationTestContext.store("locale", locale)
        integrationTestContext.store(item = csvTestsParameterFiller.getDealUiModelFromCsv(dataId))

        val url = selectUrl(locale)
        crmLoginSteps.login(
            url,
            commonProperties.newCrm.username,
            commonProperties.newCrm.password
        )

        crmMainSteps.openScreen(item = CrmMainMenuItems.Deals)
        crmMainSteps.waitTillScreenLoaded(item = CrmMainMenuItems.Deals)
        crmDealsSteps.createDealFromContext()
        crmDealCardSteps.fillDealFieldsFromContext()
        crmDealCardSteps.addProductFromContext()
        crmQuoteItemCardSteps.setQuoteItemTeam()
        crmQuoteItemCardSteps.setGeneralInfoFields()
        crmQuoteItemCardSteps.setDeliveryFields()
        crmQuoteItemCardSteps.setPricingFields()
        crmQuoteItemCardSteps.rememberDealNumber()

        crmQuoteItemCardSteps.sendToSap()
        crmDealsSteps.checkGtmNumber()
        crmQuoteItemCardSteps.goToTheDealFromProduct()
        crmDealsSteps.checkDealAndProductStatus(CrmDealStatus.UPLOADED_TO_SAP)
    }

    @ParameterizedTest(name = "Продукт успешно добавляется в сделку СХ")
    @ArgumentsSource(CrmEcomArgProviders.DealsDataCxSpot::class)
    @Owner("osipovdma")
    @Tags(value = [Tag("regress"), Tag("run")])
    @AllureId("3825")
    fun testAddNewQuoteItemToCxDeal(locale: String, dataId: String) {
        // Создаём объект сделки из параметров, прочитанных в csv файле и сторим в integrationTestContext
        integrationTestContext.store(item = csvTestsParameterFiller.getDealUiModelFromCsv(dataId))

        val url = selectUrl(locale)
        integrationTestContext.store("locale", locale)

        // Выполнение сценария создания сделки
        crmLoginSteps.login(
            url,
            commonProperties.newCrm.username,
            commonProperties.newCrm.password
        )

        crmMainSteps.openScreen(item = CrmMainMenuItems.Deals)
        crmMainSteps.waitTillScreenLoaded(item = CrmMainMenuItems.Deals)
        crmDealsSteps.createDealFromContext()
        crmDealCardSteps.fillDealFieldsFromContext()

        // Добавление продукта в сделку
        crmDealCardSteps.addProductFromContext()
        crmQuoteItemCardSteps.goToTheDealFromProduct()
        baseWebSteps.refreshPage()

        // Проверяем что в сделке есть добавленный продукт
        crmDealCardSteps.checkForProductInDeal()
    }

    @ParameterizedTest(name = "Появляется RCM номер при успешном создании ДС для сделки СХ")
    @ArgumentsSource(CrmEcomArgProviders.LocaleArgProv::class)
    @Owner("osipovdma")
    @Tags(value = [Tag("regress"), Tag("web"), Tag("run")])
    @AllureId("3797")
    fun testGetRcmNumberForCxDealDs() {
        val locale: String = TestConsts.RU
        val url = selectUrl(locale)
        integrationTestContext.store("locale", locale)

        val deal = CrmDealUiModel()
        val dealCommand = csvTestsParameterFiller.getDealUiModelFromCsv(CrmEcomArgProviders.CX_SPOT_RUS_T4).products[0].commandInfoUiModel
        deal.siFlg = false
        deal.products.add(QuoteUiModel())
        deal.contract = "СХ.16017"
        deal.products[0].generalInfoTabUiModel.additionalAgreementDate = SimpleDateFormat("dd.MM.yyyy").format(Date())
        deal.products[0].pricingTabUiModel.logistic = "1000"
        deal.products[0].commandInfoUiModel.fm = QuoteCommandInfoUiModel.Member(name = dealCommand.fm?.name)
        deal.products[0].commandInfoUiModel.jtHead = QuoteCommandInfoUiModel.Member(name = dealCommand.jtHead?.name)
        deal.products[0].commandInfoUiModel.jtBack = QuoteCommandInfoUiModel.Member(name = dealCommand.jtBack?.name)
        integrationTestContext.store(item = deal)

        // Выполнение сценария создания ДС к сделке
        crmLoginSteps.login(
            url,
            commonProperties.newCrm.username,
            commonProperties.newCrm.password
        )

        crmMainSteps.openScreen(item = CrmMainMenuItems.Deals)
        crmMainSteps.waitTillScreenLoaded(item = CrmMainMenuItems.Deals)

        // Осуществляем поиск в зависимости от локали и потом открываем первую делку
        crmDealsSteps.searchAndSelectDealByFilter(AliasConsts.CLIENT, CrmDealStatus.UPLOADED_TO_SAP.rus)

        // Копируем сделку и отправляем в САП
        crmDealCardSteps.copyDeal()
        crmDealCardSteps.fillDealFieldsFromContext()
        crmQuoteItemCardSteps.openProductInDeal()
        crmQuoteItemCardSteps.setGeneralInfoFields()
        crmQuoteItemCardSteps.setQuoteItemTeam()
        crmQuoteItemCardSteps.setDeliveryFields()
        crmQuoteItemCardSteps.setPricingFields()
        crmQuoteItemCardSteps.sendToSap()
        crmQuoteItemCardSteps.goToTheDealFromProduct()

        // Создаём ДС для открывшейся сделки
        crmDealCardSteps.addNewDs()
        baseWebSteps.refreshPage()

        // Проверяем что появился RCM номер
        crmQuoteItemCardSteps.openProductInDeal()
        crmDealsSteps.checkRcmNumber()
    }

    @ParameterizedTest(name = "Создание сделки СХ и успешная отправка в САП несколько позиций продукта")
    @ArgumentsSource(CrmEcomArgProviders.DealsDataCxSpot::class)
    @Owner("osipovdma")
    @Tags(value = [Tag("regress"), Tag("web"), Tag("run")])
    @AllureId("10634")
    fun testCreateCxDealWithTwoProds(locale: String, dataId: String) {
        Allure.parameter(AliasConsts.LOCALE, locale)
        integrationTestContext.store("locale", locale)
        integrationTestContext.store(item = csvTestsParameterFiller.getDealUiModelFromCsv(dataId))

        val url = selectUrl(locale)
        crmLoginSteps.login(
            url,
            commonProperties.newCrm.username,
            commonProperties.newCrm.password
        )

        crmMainSteps.openScreen(item = CrmMainMenuItems.Deals)
        crmMainSteps.waitTillScreenLoaded(item = CrmMainMenuItems.Deals)
        crmDealsSteps.createDealFromContext()
        crmDealCardSteps.fillDealFieldsFromContext()

        for (i: Int in (1..2)) {
            var changedDeal = integrationTestContext.fetch(clazz = CrmDealUiModel::class)
            changedDeal.products[0].generalInfoTabUiModel.volume = (20..900).random().toString()
            integrationTestContext.store(item = changedDeal)

            crmDealCardSteps.addProductFromContext()
            crmQuoteItemCardSteps.setQuoteItemTeam()
            crmQuoteItemCardSteps.setGeneralInfoFields()
            crmQuoteItemCardSteps.setDeliveryFields()
            crmQuoteItemCardSteps.setPricingFields()
            crmQuoteItemCardSteps.goToTheDealFromProduct()
        }

        crmDealCardSteps.sendToSapFromDeal()
        crmDealsSteps.checkDealAndProductStatus(CrmDealStatus.UPLOADED_TO_SAP)
    }

    @Test
    @Owner("osipovdma")
    @DisplayName("При отправки сделки в САП подтягивается себестоимость по валюте, дате, и заводу")
    @Tags(Tag("manual"), Tag("crm-2170"))
    @Manual
    @AllureId("17216")
    fun testCheckForCostPriceWithPlant() {
        Allure.step("Создать СХ сделку")
        Allure.step("В админке добавить себестоимость превышающую цену прайса с указанием завода как в сделке")
        Allure.step("Кликнуть кнопку Отправить в САП")
        Allure.step("Убедиться, что появляется окно с информацие об отрицательной марже и ценой из добавленной в админке себестоимост")
    }

    @Test
    @Owner("osipovdma")
    @DisplayName("При отправки сделки в САП подтягивается себестоимость по валюте и дате, без завода")
    @Tags(Tag("manual"), Tag("crm-2170"))
    @Manual
    @AllureId("17217")
    fun testCheckForCostPriceWithoutPlant() {
        Allure.step("Создать СХ сделку")
        Allure.step("В админке добавить себестоимость превышающую цену прайса без указания завода")
        Allure.step("Кликнуть кнопку Отправить в САП")
        Allure.step("Убедиться, что появляется окно с информацие об отрицательной марже и ценой из добавленной в админке себестоимости")
    }

    @Test
    @Owner("osipovdma")
    @DisplayName("Выполняется сравнение цены прайса и себестоимости во время отправки в САП")
    @Tags(Tag("manual"), Tag("crm-2170"))
    @Manual
    @AllureId("17218")
    fun testCheckForCostPriceOnSapSending() {
        Allure.step("Убедиться что выполняется сравнение прайса и себестоимости при отправки сделки в САП с позиции продукта")
        Allure.step("Убедиться что выполняется сравнение прайса и себестоимости при отправки сделки в САП с позиции сделки")
        Allure.step("Убедиться что выполняется сравнение прайса и себестоимости при отправки сделки в САП по клику на кнопку Согласовать")
    }

}