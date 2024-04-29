function getUserTimeZoneId() {
  const timeZoneId = Intl.DateTimeFormat().resolvedOptions().timeZone
  return timeZoneId
}

function getUserTimeZoneLocale() {
  const timeZoneLocale = Intl.DateTimeFormat().resolvedOptions().locale
  return timeZoneLocale
}

function UTCtoLocalFormat(utcTimeString) {
  let utcDate
  utcDate = new Date(utcTimeString)

  const offsetMinutes = utcDate.getTimezoneOffset()

  const localDate = new Date(utcDate.getTime() - offsetMinutes * 60000)
  const day = String(localDate.getDate()).padStart(2, '0')
  const month = String(localDate.getMonth() + 1).padStart(2, '0')
  const year = localDate.getFullYear()
  const hours = String(localDate.getHours()).padStart(2, '0')
  const minutes = String(localDate.getMinutes()).padStart(2, '0')
  const seconds = String(localDate.getSeconds()).padStart(2, '0')

  const formattedDate = `${day}/${month}/${year}, ${hours}:${minutes}:${seconds}`

  return formattedDate
}

// Example usage:
// console.log(getUserTimeZoneId())
// console.log(UTCtoLocalFormat('2024-04-22T09:30:00'))
// console.log(UTCtoLocalFormat('2024-04-22T09:00:00Z'))

export { getUserTimeZoneId, getUserTimeZoneLocale, UTCtoLocalFormat }
