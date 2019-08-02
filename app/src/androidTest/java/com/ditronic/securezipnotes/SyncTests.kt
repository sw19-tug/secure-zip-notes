package com.ditronic.securezipnotes

import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.ditronic.securezipnotes.activities.MainActivity
import com.ditronic.securezipnotes.util.TestUtil
import com.ditronic.simplefilesync.AbstractFileSync
import com.ditronic.simplefilesync.DropboxFileSync
import com.ditronic.simplefilesync.util.ResultCode
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import targetContext

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
class SyncTests {

    companion object {
        private const val DROPBOX_OAUTH_TOKEN = "T6OO59Oo9FoAAAAAAAANTySOeCziL-1_agAU2sr2mU8ArSZqr3RKb6ICU5a_JJVt"
    }

    @get:Rule
    var acRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java, false, false)

    @Before
    fun beforeEachTest() {
        TestUtil.isInstrumentationTest = true
        DropboxFileSync.storeNewOauthToken(DROPBOX_OAUTH_TOKEN, targetContext())
    }

    @Test
    fun dbx1_dropBoxUpload() {
        // TODO: Fix this upload test by uploading something new each time
        precondition_cleanStart(acRule)
        Espresso.onIdle()
        Assert.assertEquals(ResultCode.UPLOAD_SUCCESS, AbstractFileSync.getLastSyncResult()!!.resultCode)
    }

    @Test
    fun dbx2_dropBoxRemoteEqualsLocal() {
        precondition_singleNote(acRule)
        Espresso.onIdle()
        Assert.assertEquals(ResultCode.REMOTE_EQUALS_LOCAL, DropboxFileSync.getLastSyncResult()!!.resultCode)
    }

    @Test
    fun dbx3_dropBoxDownload() {
        precondition_cleanStart(acRule)
        Espresso.onIdle()
        Assert.assertEquals(ResultCode.DOWNLOAD_SUCCESS, DropboxFileSync.getLastSyncResult()!!.resultCode)
        main_assertNonEmpty(acRule.activity)
    }
}
