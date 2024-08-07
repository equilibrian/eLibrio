package su.elibrio.mobile.ui.behavior

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import su.elibrio.mobile.viewmodel.MainActivityViewModel

class MainScrollConnection @OptIn(ExperimentalMaterial3Api::class) constructor(
    private val scrollBehavior: TopAppBarScrollBehavior,
    private val viewModel: MainActivityViewModel
) : NestedScrollConnection {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val scrollBehaviorOffset =
            scrollBehavior.nestedScrollConnection.onPreScroll(available, source)

        val isTopBarCollapsed = scrollBehavior.state.collapsedFraction == 1f

        if (available.y < -24 && viewModel.isFiltersVisible.value == true && isTopBarCollapsed)
            viewModel.hideFilters()
        else if (available.y > 1 && viewModel.isFiltersVisible.value == false)
            viewModel.showFilters()

        return scrollBehaviorOffset
    }
}