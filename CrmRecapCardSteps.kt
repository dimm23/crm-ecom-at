package ru.sibur.test.crm_ecom_at.tests.steps.web.crm

import io.qameta.allure.Allure
import io.qameta.allure.Step
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.sibur.test.crm_ecom_at.common.model.crm.deal.CrmDealUiModel
import ru.sibur.test.crm_ecom_at.tests.context.IntegrationTestContext
import ru.sibur.test.crm_ecom_at.tests.steps.web.BaseWebSteps

@Component
class CrmRecapCardSteps {
    /**
     * Шаги внутри карточки сделки.
     */

    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var baseWebSteps: BaseWebSteps
    @Autowired
    private lateinit var integrationTestContext: IntegrationTestContext

    @Step("Копирование рекапа")
    fun copyRecap() {
        // копирование рекапа
        log.info("Нажатие кнопки \"Копировать\"")
        baseWebSteps.ui().crmRecapWebUtil().copyRecapBtnClick()

        // ожидание открытия карточки рекапа
        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
        baseWebSteps.ui().crmRecapWebUtil().waitRecapCardIsLoaded()
    }

    @Step("Отправка рекапа в ЦКС")
    fun sendToSCS() {
        log.info("Нажатие кнопки \"Отправить в ЦКС\"")
        baseWebSteps.ui().crmRecapWebUtil().sendToScsBtnClick()
        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
    }

    @Step("Взять сделку из рекапа в работу")
    fun takeDealFromRecapInProcess() {
        log.info("Нажатие кнопки 'Взять в работу'")
        baseWebSteps.ui().crmRecapWebUtil().takeInProgressBtnClick()
        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
    }

    @Step("Запоминаем номер рекапа")
    fun rememberRecapNumber() {
        val recapNumber: String = baseWebSteps.ui().crmRecapWebUtil().getRecapNumber()
        integrationTestContext.store("recapNumber", item = recapNumber)
    }

    @Step("Открываем продукт в рекапе")
    fun openProductPositionInRecap(index: Int = 0) {
        log.info("Открытие позиции рекапа № $index")
        baseWebSteps.ui().crmRecapWebUtil().openRecapPositionByIndex(index)
    }

    @Step("Переходим из продукта в рекап")
    fun goToTheRecapFromProduct() {
        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
        baseWebSteps.ui().crmRecapWebUtil().goBackFromProductToDeal()
        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
    }

    @Step("Заполняем вкладку ценообразование")
    fun setPricingFields(comment: String? = null) {
        val deal: CrmDealUiModel = if (comment != null) {
            integrationTestContext.fetch(alias = comment, clazz = CrmDealUiModel::class)
        } else {
            integrationTestContext.fetch(clazz = CrmDealUiModel::class)
        }
        Allure.step("Проверка развернутости вкладки \"Ценообразование\"", Allure.ThrowableRunnable {
            if (!baseWebSteps.ui().crmQuoteItemCardUtil().isPricingTabExpanded()) {
                baseWebSteps.ui().crmQuoteItemCardUtil().openPricingTab()
            }})
        val pricingTab = deal.products[0].pricingTabUiModel
        //
        var price: String? = null
        if (pricingTab.priceListPrice == null) price = pricingTab.totalPriceWithVat
        //
        baseWebSteps.ui().crmRecapWebUtil().fillPricingFields(
            paymentCondition = pricingTab.paymentCondition.code,
            priceListPrice = pricingTab.priceListPrice,
            countryForTaxDefinition = pricingTab.countryForTaxDefinition,
            deliveryIncluded = pricingTab.deliveryIncluded,
            premiumOrDiscountList = pricingTab.premiumOrDiscountList,
            manualPriceInput = price,
            logistic = pricingTab.logistic,
        )
        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
    }

    @Step("Заполнение вкладки Доставка")
    fun setDeliveryFields(comment: String? = null){
        val deal: CrmDealUiModel = if (comment != null) {
            integrationTestContext.fetch(alias = comment, clazz = CrmDealUiModel::class)
        } else {
            integrationTestContext.fetch(clazz = CrmDealUiModel::class)
        }
        Allure.step("Проверка развернутости вкладки \"Доставка\"", Allure.ThrowableRunnable {
            if (!baseWebSteps.ui().crmQuoteItemCardUtil().isDeliveryTabExpanded()) {
                log.info("Открываем вкладку Доставка")
                baseWebSteps.ui().crmQuoteItemCardUtil().openDeliveryTab()
            }})

        val deliveryTab = deal.products[0].deliveryTabUiModel

        baseWebSteps.ui().crmRecapWebUtil().fillDeliveryFields(
            shippingMethod = deliveryTab.shippingMethod,
            detailedShippingMethod = deliveryTab.detailedShippingMethod,
            consignee = deliveryTab.consignee,
            basis = deliveryTab.basis,
            shippingCondition = deliveryTab.shipmentCondition,
            destination = deliveryTab.destination,
            customsStatus = deliveryTab.customsStatus,
            warehouse = deliveryTab.warehouse,
        )
    }
}