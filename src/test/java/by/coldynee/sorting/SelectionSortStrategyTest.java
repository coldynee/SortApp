package by.coldynee.sorting;

class SelectionSortStrategyTest extends AbstractSortingStrategyTest {

    @Override
    protected SortingStrategy createStrategy() {
        return new SelectionSortStrategy();
    }

    @Override
    protected String getExpectedName() {
        return "Сортировка выбором";
    }

    @Override
    protected boolean isStable() {
        return false;
    }
}