package ir.jatlin.topmarket.core.data.source.remote

import com.google.common.truth.Truth
import ir.jatlin.core.network.di.NetworkModule
import ir.jatlin.topmarket.rule.MainCoroutineRule
import ir.jatlin.topmarket.rule.runBlockingTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductResponseTest {

    private val gson = NetworkModule.provideGson()

    private lateinit var source: FakeProductRemoteDataSource

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        source = FakeProductRemoteDataSource(
            coroutineRule.testDispatcher,
            gson
        )
    }

    @Test
    fun `get product details by id and return successful`() =
        coroutineRule.runBlockingTest {
            val productDetails = source.findProductDetailsById(677)
            Truth.assertThat(productDetails).isNotNull()
            Truth.assertThat(productDetails.categories).hasSize(2)
            Truth.assertThat(productDetails.images).hasSize(3)
        }

    @Test
    fun `get list of products and return successful`() =
        coroutineRule.runBlockingTest {
            val productList = source.getProductsList(page = 1, null, null)
            Truth.assertThat(productList).isNotNull()

            val product = productList.first()
            Truth.assertThat(product.id).isEqualTo(677)
            Truth.assertThat(product.categories).hasSize(2)
            Truth.assertThat(product.price).isEqualTo("2060000")
            Truth.assertThat(product.description).isNotEmpty()
            Truth.assertThat(product.images).hasSize(3)
        }

    @Test
    fun `get product categories and return successful`() =
        coroutineRule.runBlockingTest {
            val categories = source.getProductCategories(
                page = 1, null, null
            )
            Truth.assertThat(categories).isNotNull()
            Truth.assertThat(categories).isNotEmpty()
            Truth.assertThat(categories.first().id).isEqualTo(121)
        }

}