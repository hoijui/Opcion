august 7, 2001
##############

launching browser from Java

compiled with J2SE 1.3.1
source code included.

usage:
 com.xlreader.shared.io.S_ToBrowserDefault.s_displayURL("http://www.javasoft.com")

source: S_ToBrowserDefault.java (located in com.xlreader.shared.io)

O.S. limitations:
. Windows: open up default browser (or default mailer)
. Linux/Unix:
  . open up Netscape browser (or Netscape messenger)
  . if sending a mail that contains a subject with multiple words, only the first word of the subject is passed to Netscape messenger.
