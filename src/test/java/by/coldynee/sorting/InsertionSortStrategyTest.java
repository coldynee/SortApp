package by.coldynee.sorting;

class InsertionSortStrategyTest extends AbstractSortingStrategyTest {

    @Override
    protected SortingStrategy createStrategy() {
        return new InsertionSortStrategy();
    }

    @Override
    protected String getExpectedName() {
        return "Сортировка вставками";
    }

    @Override
    protected boolean isStable() {
        return true;
    }
}