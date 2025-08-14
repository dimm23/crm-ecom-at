package ru.sibur.test.crm_ecom_at.tests.steps.web.crm

import io.qameta.allure.Allure
import io.qameta.allure.Step
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.sibur.test.crm_ecom_at.common.AutoTestException
import ru.sibur.test.crm_ecom_at.common.model.AliasConsts
import ru.sibur.test.crm_ecom_at.common.model.TestConsts
import ru.sibur.test.crm_ecom_at.common.model.crm.deal.CrmDealUiModel
import ru.sibur.test.crm_ecom_at.common.model.enums.crm.deal.CrmDealStatus
import ru.sibur.test.crm_ecom_at.common.model.enums.crm.deal.CrmDealType
import ru.sibur.test.crm_ecom_at.tests.context.IntegrationTestContext
import ru.sibur.test.crm_ecom_at.tests.steps.web.BaseWebSteps

@Component
class CrmDealsSteps {
    /**
     * Шаги на экране 'Сделки'
     */

    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var baseWebSteps: BaseWebSteps
    @Autowired
    private lateinit var integrationTestContext: IntegrationTestContext

    @Step("Создание сделки и заполнение попапа Новая сделка")
    fun createDealFromContext() {
        val deal = integrationTestContext.fetch(clazz = CrmDealUiModel::class)
        Allure.step("Нажатие кнопки \"Новая сделка\"")
        baseWebSteps.ui().crmDealsUtil().createDealBtnClick()
        baseWebSteps.ui().crmCreateDealPopupUtil().createDealPopupSetFields(
            client = deal.account,
            currency = deal.currency,
            channel = deal.channel,
            contract = deal.contract,
            salesOffice = deal.salesOffice,
            salesOrganization = deal.salesOrg,
            siFlg = deal.siFlg,
            clickNextBtn = true,
        )
        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
        log.info("Окончание загрузки")
    }

    @Step("Использовать фильтр по номеру сделки на экране \"Сделки\" и переход в карточку найденной сделки")
    fun openDealByDealNumber(number: String? = null) {
        val dealNumber: String = if(!number.isNullOrEmpty()) {number} else {
            integrationTestContext.fetch(AliasConsts.DEAL_NUMBER, clazz = String::class)
        }
        baseWebSteps.ui().crmDealsUtil().switchToAllTab()
        baseWebSteps.ui().crmDealsUtil().fillFilterFieldsAndApply(
            dealNum = dealNumber,
            applyBtnClick = true
        )
        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()

        // получить список элементов, убедиться, что элемент один
        val a = baseWebSteps.ui().crmDealsUtil().getPresentedElements(expand = true)
        if (a.size != 1) throw AutoTestException("Поиск сделки с номером $dealNumber не вернул уникальное значение, " +
                "Представлены элененты: $a")

        // открыть сделку первую в списке
        baseWebSteps.ui().crmDealsUtil().openFirstDeal()
        baseWebSteps.ui().crmDealCardUtil().waitDealCardIsLoaded()
    }

    fun searchAndSelectDealByFilter(client: String, status: String, source: String? = CrmDealType.CRM.title, dealRow: Int = 0) {
        Allure.step("Выполняется поиск сделки со статусом $status", Allure.ThrowableRunnable {
            baseWebSteps.ui().crmDealsUtil().switchToAllTab()
            baseWebSteps.ui().crmDealsUtil().fillFilterFieldsAndApply(
                client = client,
                source = source,
                status = status,
                applyBtnClick = true
            )
            baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
            baseWebSteps.ui().crmDealsUtil().openDealByRowNumber(dealRow)
            baseWebSteps.ui().crmDealCardUtil().waitDealCardIsLoaded()
        })
    }

    @Step("Выполняем поиск сделки с GTM номером {gtmNumber}")
    fun searchDealByGtmNumber(gtmNumber: String) {
        baseWebSteps.ui().crmDealsUtil().switchToAllTab()
        baseWebSteps.ui().crmDealsUtil().fillFilterFieldsAndApply(
            gtmNum = gtmNumber,
            applyBtnClick = true
        )
        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
        baseWebSteps.ui().crmDealsUtil().openFirstDeal()
        baseWebSteps.ui().crmDealCardUtil().waitDealCardIsLoaded()
    }

    @Step("Выполняем поиск сделки с номером {dealNumber}")
    fun searchDealByNumber(dealNumber: String) {
        baseWebSteps.ui().crmDealsUtil().switchToAllTab()
        baseWebSteps.ui().crmDealsUtil().fillFilterFieldsAndApply(
            dealNum = dealNumber,
            applyBtnClick = true
        )
        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
        baseWebSteps.ui().crmDealsUtil().openFirstDeal()
        baseWebSteps.ui().crmDealCardUtil().waitDealCardIsLoaded()
    }

    @Step("Проверка GTM номера")
    fun checkGtmNumber() {
        Allure.step("Проверка присвоения GTM", Allure.ThrowableRunnable {
            baseWebSteps.ui().crmQuoteItemCardUtil().openGeneralInformationTab()

            // запрос инфы со вкладки Основной информации
            val gtm = baseWebSteps.ui().crmQuoteItemCardGeneralInformationTabUtil().getGeneralInformationTabInfo().gtmNum
            if (gtm!!.length != 8) {
                Allure.step("Первый вызов: GTM номер - ${gtm} некорректной длины, обновляем страницу и пробуем считать GTM номер ещё раз.")
                // Повторное открытие вкладки Основная информация, для обновления страницы. На случай если что-то не подтянулось с первого раза
                baseWebSteps.ui().crmQuoteItemCardUtil().openGeneralInformationTab()
                // Повторный вызов метода
                val newGtm = baseWebSteps.ui().crmQuoteItemCardGeneralInformationTabUtil().getGeneralInformationTabInfo().gtmNum
                if (newGtm!!.length != 8) {
                    throw AutoTestException("GTM, ожидается 8 символов, в наличии - ${newGtm}")
                } else {
                    Allure.step("Повторный вызов: получен корректный GTM номер - ${newGtm}")
                }
            } else {
                Allure.step("Получен GTM номер - ${gtm}")
            }
        })
    }

    @Step("Проверяем соответствие статуса сделки")
    fun checkDealAndProductStatus(status: CrmDealStatus) {
        var statusValue: String = ""
        val locale = integrationTestContext.fetch("locale", String::class)

        if (locale == TestConsts.EN) {
            statusValue = status.eng
        } else {
            statusValue = status.rus
        }
        Allure.step("Ожидаем что статус ${statusValue}")
        baseWebSteps.ui().crmDealCardUtil().waitDealCardIsLoaded()
        baseWebSteps.ui().crmDealCardUtil().assertThatDealAndProductHasStatus(statusValue)
    }

    @Step("Проверяем присвоение RCM номера")
    fun checkRcmNumber() {
        baseWebSteps.ui().crmQuoteItemCardUtil().openGeneralInformationTab()
        var rcm = baseWebSteps.ui().crmQuoteItemCardGeneralInformationTabUtil().getGeneralInformationTabInfo().rcmNum

        // Ожидание присвоения RCM номера
        var i = 15
        while (rcm!!.isEmpty()) {
            if (i > 0) {
                // В течении $i секунд ожидаем появления RCM номера
                Thread.sleep(1000)
                i--
                baseWebSteps.refreshPage()
                baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
                rcm = baseWebSteps.ui().crmQuoteItemCardGeneralInformationTabUtil().getGeneralInformationTabInfo().rcmNum
            } else {break}
        }

        baseWebSteps.ui().softly.assertThat(rcm.length).`as`("RCM, ожидается 12 символов, в наличии - ${rcm.length}").isEqualTo(12)
        Allure.step("Получен RCM номер - ${rcm}")
    }

}