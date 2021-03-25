package me.jacobrr.whatsappanalyzer.util

import kotlinx.coroutines.*

fun <R> CoroutineScope.executeAsyncTask(
        onPreExecute: () -> Unit,
        doInBackground: () -> R,
        onPostExecute: (R) -> Unit,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
) = launch {
    onPreExecute() // runs in Main Thread
    val result = withContext(dispatcher) {
        doInBackground() // runs in background thread without blocking the Main Thread
    }
    onPostExecute(result) // runs in Main Thread
}

fun <P, R> CoroutineScope.executeAsyncTask(
        onPreExecute: () -> Unit,
        doInBackground: suspend (suspend (P) -> Unit) -> R,
        onPostExecute: (R) -> Unit,
        onProgressUpdate: (P) -> Unit,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
) = launch {
    onPreExecute()

    val result = withContext(dispatcher) {
        doInBackground {
            withContext(Dispatchers.Main) { onProgressUpdate(it) }
        }
    }
    onPostExecute(result)
}