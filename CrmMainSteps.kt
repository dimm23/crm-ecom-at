package ru.sibur.test.crm_ecom_at.tests.steps.web.crm

import io.qameta.allure.Allure
import io.qameta.allure.Step
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.sibur.test.crm_ecom_at.common.model.enums.crm.CrmMainMenuItems
import ru.sibur.test.crm_ecom_at.common.properties.CommonProperties
import ru.sibur.test.crm_ecom_at.tests.steps.web.BaseWebSteps

@Component
class CrmMainSteps {
    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var commonProperties: CommonProperties

    @Autowired
    private lateinit var baseWebSteps: BaseWebSteps

    fun openScreen(
        item: CrmMainMenuItems
    ) {
        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
        log.info("Открытие экрана - $item")
        when (item) {
            CrmMainMenuItems.Home -> {
                Allure.step("Открыть экран - \"Домой\"",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmMainUtil().openHomeScreen()
                    })
            }
            CrmMainMenuItems.Inbox -> {
                Allure.step("Открыть экран - \"Входящие\"",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmMainUtil().openInboxScreen()
                    })
            }
            CrmMainMenuItems.CLIENTS -> {
                baseWebSteps.ui().crmMainUtil().openClientsScreen()
            }
            CrmMainMenuItems.Opportunities -> {
                Allure.step("Открыть экран - \"Возможности\"",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmMainUtil().openOpportunitiesScreen()
                    })
            }
            CrmMainMenuItems.Activities -> {
                Allure.step("Открыть экран - \"Активности\"",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmMainUtil().openActivitiesScreen()
                    })
            }
            CrmMainMenuItems.Reports -> {
                Allure.step("Открыть экран - \"Отчеты\"",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmMainUtil().openReportsScreen()
                    })
            }
            CrmMainMenuItems.ReportsTableau -> {
                Allure.step("Открыть экран - \"Отчеты Tableau\"",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmMainUtil().openReportsTableauScreen()
                    })
            }
            CrmMainMenuItems.Deals -> {
                Allure.step("Открыть экран - \"Сделки\"",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmMainUtil().openDealsScreen()
                    })
            }
            CrmMainMenuItems.Recaps -> {
                Allure.step("Открыть экран - 'Рекапы'", Allure.ThrowableRunnableVoid {
                    baseWebSteps.ui().crmMainUtil().openRecapsScreen()
                })
            }
            CrmMainMenuItems.REQUESTS -> {
                baseWebSteps.ui().crmMainUtil().openRequestsScreen()
            }
            CrmMainMenuItems.Investigations -> {
                Allure.step("Открыть экран - \"Расследования\"",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmMainUtil().openInvestigationsScreen()
                    })
            }
            CrmMainMenuItems.Prospect -> {
                Allure.step("Открыть экран - \"Лидогенерация\"",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmMainUtil().openProspectScreen()
                    })
            }
            CrmMainMenuItems.GroupOfCompanies -> {
                Allure.step("Открыть экран - \"Группы компаний\"",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmMainUtil().openGroupsOfCompaniesScreen()
                    })
            }
            CrmMainMenuItems.CROSSSALES -> {
                baseWebSteps.ui().crmMainUtil().openCrossSalesScreen()
            }
            CrmMainMenuItems.SERVICES -> {
                baseWebSteps.ui().crmMainUtil().openServicesScreen()
            }
        }
    }

    fun waitTillScreenLoaded(
        item: CrmMainMenuItems
    ) {
        log.info("Ожидание отрисовки экрана - $item")
        when (item) {
            CrmMainMenuItems.Home -> {
                Allure.step("Дождаться загрузки экрана - \"Домой\"",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
                        baseWebSteps.ui().crmMainUtil().waitHomeScreenLoaded()
                    })
            }
            CrmMainMenuItems.Inbox -> {
                Allure.step("Дождаться загрузки экрана - \"Входящие\"",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
                        baseWebSteps.ui().crmMainUtil().waitInboxScreenLoaded()
                    })
            }
            CrmMainMenuItems.CLIENTS -> {
                Allure.step("Дождаться загрузки экрана - \"Клиенты\"",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
                        baseWebSteps.ui().crmMainUtil().waitClientsScreenLoaded()
                    })
            }
            CrmMainMenuItems.Opportunities -> {
                Allure.step("Дождаться загрузки экрана - \"Возможности\"",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
                        baseWebSteps.ui().crmMainUtil().waitOpportunitiesScreenLoaded()
                    })
            }
            CrmMainMenuItems.Activities -> {
                Allure.step("Дождаться загрузки экрана - \"Активности\"",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
                        baseWebSteps.ui().crmMainUtil().waitActivitiesScreenLoaded()
                    })
            }
            CrmMainMenuItems.Reports -> {
                Allure.step("Дождаться загрузки экрана - \"Отчеты\"",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
                        baseWebSteps.ui().crmMainUtil().waitReportsScreenLoaded()
                    })
            }
            CrmMainMenuItems.ReportsTableau -> {
                Allure.step("Дождаться загрузки экрана - \"Отчеты Tableau\"",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
                        baseWebSteps.ui().crmMainUtil().waitReportsTableauScreenLoaded()
                    })
            }
            CrmMainMenuItems.Deals -> {
                Allure.step("Дождаться загрузки экрана - \"Сделки\"",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
                        baseWebSteps.ui().crmMainUtil().waitDealsScreenLoaded()
                    })
            }
            CrmMainMenuItems.Recaps -> {
                Allure.step("Дождаться загрузки экрана - 'Рекапы'", Allure.ThrowableRunnableVoid {
                    baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
                    baseWebSteps.ui().crmMainUtil().waitRecapsScreenLoaded()
                })
            }
            CrmMainMenuItems.REQUESTS -> {
                Allure.step("Дождаться загрузки экрана - \"Обращения\"",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
                        baseWebSteps.ui().crmMainUtil().waitRequestsScreenLoaded()
                    })
            }
            CrmMainMenuItems.Investigations -> {
                Allure.step("Дождаться загрузки экрана - \"Расследования\"",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
                        baseWebSteps.ui().crmMainUtil().waitInvestigationsScreenLoaded()
                    })
            }
            CrmMainMenuItems.Prospect -> {
                Allure.step("Дождаться загрузки экрана - \"Лидогенерация\"",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
                        baseWebSteps.ui().crmMainUtil().waitProspectScreenLoaded()
                    })
            }
            CrmMainMenuItems.GroupOfCompanies -> {
                Allure.step("Дождаться загрузки экрана - \"Группы компаний\"",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
                        baseWebSteps.ui().crmMainUtil().waitGroupsOfCompaniesScreenLoaded()
                    })
            }
            CrmMainMenuItems.CROSSSALES -> {
                Allure.step("Дождаться загрузки экрана - \"Кросс-продажи\"",
                    Allure.ThrowableRunnableVoid {
                        baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
                        baseWebSteps.ui().crmMainUtil().waitCrossSalesScreenLoaded()
                    })
            }
        }
        log.info("Экран успешно отобразился - $item")
    }

    @Step("Выключаю умный поиск и ввожу запрос {request}")
    fun offSmartSearchAndFind(request: String) {
        with(baseWebSteps.ui().crmMainUtil()) {
            baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
            offSmartSearch()
            search(request)
        }
    }

    @Step("Включаю умный поиск и ввожу запрос {request}")
    fun onSmartSearchAndFind(request: String) {
        with(baseWebSteps.ui().crmMainUtil()) {
            baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
            onSmartSearch()
            search(request)
        }
    }

    @Step("Включаю умный поиск, ввожу запрос {request} и жму ENTER")
    fun onSmartSearchAndFindAndPressEnter(request: String) { //ToDo можно сделать boolean
        with(baseWebSteps.ui().crmMainUtil()) {
            baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
            onSmartSearch()
            searchAndPressEnter(request)

        }
    }

    @Step("Включаю умный поиск, ввожу запрос {request}, жму ENTER и проваливаюсь в клиента")
    fun onSmartSearchAndFindAndOpenClient(request: String) { //ToDo можно сделать boolean
        with(baseWebSteps.ui().crmMainUtil()) {
            baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
            onSmartSearch()
            searchPressEnterAndOpenClient(request)
        }
    }

    @Step("Выключаю чекбоксы после поиска")
    fun offCheckBoxWhenSearch() {
        with(baseWebSteps.ui().crmMainUtil()) {
            baseWebSteps.ui().crmBaseUtil().waitUiIsReady()
            checkboxSearchOff()
            checkboxSearchOn()

        }
    }

    @Step("Разлогин через добавление команды \"Logoff\"")
    fun logoff() {
        log.info("Разлогин через добавление команды \"Logoff\"")
        val curUrl = baseWebSteps.ui().wd.currentUrl
        baseWebSteps.open("${curUrl.substring(startIndex = 0, endIndex = curUrl.indexOf("?") + 1)}&SWECmd=Logoff")
        Thread.sleep(500)
    }

}