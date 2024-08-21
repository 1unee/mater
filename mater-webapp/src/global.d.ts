// global.d.ts

interface Window {
  Telegram: {
    WebApp: TelegramWebApp;
  };
}

interface WebAppInitData {
  // Define the structure of WebAppInitData here
}

interface ThemeParams {
  // Define the structure of ThemeParams here
}

// PopupParams describes the native popup.
interface PopupParams {
  title?: string; // Optional. The text to be displayed in the popup title, 0-64 characters.
  message: string; // The message to be displayed in the body of the popup, 1-256 characters.
  buttons?: PopupButton[]; // Optional. List of buttons to be displayed in the popup, 1-3 buttons. Default is [{ "type": "close" }].
}

// PopupButton describes the native popup button.
interface PopupButton {
  id?: string; // Optional. Identifier of the button, 0-64 characters. Default is an empty string.
  type?: 'default' | 'ok' | 'close' | 'cancel' | 'destructive'; // Optional. Type of the button. Default is 'default'.
  text?: string; // Optional. The text to be displayed on the button, 0-64 characters. Required if type is 'default' or 'destructive'.
}

// BackButton controls the back button in the Telegram interface.
interface BackButton {
  isVisible?: boolean; // Shows whether the button is visible. Default is false.
  onClick(callback: Function): BackButton; // Sets the button press event handler.
  offClick(callback: Function): BackButton; // Removes the button press event handler.
  show(): BackButton; // Makes the button active and visible.
  hide(): BackButton; // Hides the button.
}

// MainButton controls the main button in the Telegram interface.
interface MainButton {
  text: string; // Current button text. Default is 'CONTINUE'.
  color: string; // Current button color. Default is themeParams.button_color.
  textColor: string; // Current button text color. Default is themeParams.button_text_color.
  isVisible: boolean; // Shows whether the button is visible. Default is false.
  isActive: boolean; // Shows whether the button is active. Default is true.
  isProgressVisible: boolean; // Readonly. Shows whether the button is displaying a loading indicator.
  setText(text: string): MainButton; // Sets the button text.
  onClick(callback: Function): MainButton; // Sets the button press event handler.
  offClick(callback: Function): MainButton; // Removes the button press event handler.
  show(): MainButton; // Makes the button visible.
  hide(): MainButton; // Hides the button.
  enable(): MainButton; // Enables the button.
  disable(): MainButton; // Disables the button.
  showProgress(leaveActive?: boolean): MainButton; // Shows a loading indicator on the button. Default disables the button while loading.
  hideProgress(): MainButton; // Hides the loading indicator.
  setParams(params: {
    text?: string; // Optional. Button text.
    color?: string; // Optional. Button color.
    textColor?: string; // Optional. Button text color.
    isActive?: boolean; // Optional. Enables the button.
    isVisible?: boolean; // Optional. Shows the button.
  }): MainButton; // Sets button parameters.
}

// SettingsButton controls the Settings item in the context menu.
interface SettingsButton {
  isVisible?: boolean; // Shows whether the context menu item is visible. Default is false.
  onClick(callback: Function): SettingsButton; // Sets the press event handler for the Settings item.
  offClick(callback: Function): SettingsButton; // Removes the press event handler from the Settings item.
  show(): SettingsButton; // Makes the Settings item visible.
  hide(): SettingsButton; // Hides the Settings item.
}

// HapticFeedback controls haptic feedback.
interface HapticFeedback {
  impactOccurred(style: 'light' | 'medium' | 'heavy' | 'rigid' | 'soft'): HapticFeedback; // Tells that an impact occurred. Style indicates the type of impact.
  notificationOccurred(type: 'error' | 'success' | 'warning'): HapticFeedback; // Tells that a task or action has succeeded, failed, or produced a warning.
  selectionChanged(): HapticFeedback; // Tells that the user has changed a selection.
}


interface CloudStorage {
  // Define the structure of CloudStorage here
}

interface BiometricManager {
  // Define the structure of BiometricManager here
}

interface StoryShareParams {
  // Define the structure of StoryShareParams here
}

interface ScanQrPopupParams {
  // Define the structure of ScanQrPopupParams here
}

interface ScanQrPopupClosedEvent {
  // Define the structure of ScanQrPopupClosedEvent here
}

interface ClipboardTextReceivedEvent {
  // Define the structure of ClipboardTextReceivedEvent here
}

interface InvoiceClosedEvent {
  // Define the structure of InvoiceClosedEvent here
}

interface TelegramWebApp {
  initData: string; // A string with raw data transferred to the Mini App, convenient for validating data.
  initDataUnsafe: WebAppInitData; // An object with input data transferred to the Mini App.
  version: string; // The version of the Bot API available in the user's Telegram app.
  platform: string; // The name of the platform of the user's Telegram app.
  colorScheme: 'light' | 'dark'; // The color scheme currently used in the Telegram app.
  themeParams: ThemeParams; // An object containing the current theme settings used in the Telegram app.
  isExpanded: boolean; // True if the Mini App is expanded to the maximum available height.
  viewportHeight: number; // The current height of the visible area of the Mini App.
  viewportStableHeight: number; // The height of the visible area of the Mini App in its last stable state.
  headerColor: string; // Current header color in the #RRGGBB format.
  backgroundColor: string; // Current background color in the #RRGGBB format.
  isClosingConfirmationEnabled: boolean; // True if the confirmation dialog is enabled while closing the Mini App.
  isVerticalSwipesEnabled: boolean; // True if vertical swipes to close or minimize the Mini App are enabled.
  BackButton: BackButton; // An object for controlling the back button in the Telegram interface.
  MainButton: MainButton; // An object for controlling the main button at the bottom of the Mini App.
  SettingsButton: SettingsButton; // An object for controlling the Settings item in the context menu.
  HapticFeedback: HapticFeedback; // An object for controlling haptic feedback.
  CloudStorage: CloudStorage; // An object for controlling cloud storage.
  BiometricManager: BiometricManager; // An object for controlling biometrics on the device.
  isVersionAtLeast(version: string): boolean; // Returns true if the user's app supports a version of the Bot API that is equal to or higher than the version passed.
  setHeaderColor(color: string): void; // Sets the app header color in the #RRGGBB format.
  setBackgroundColor(color: string): void; // Sets the app background color in the #RRGGBB format.
  enableClosingConfirmation(): void; // Enables a confirmation dialog while closing the Mini App.
  disableClosingConfirmation(): void; // Disables the confirmation dialog while closing the Mini App.
  enableVerticalSwipes(): void; // Enables vertical swipes to close or minimize the Mini App.
  disableVerticalSwipes(): void; // Disables vertical swipes to close or minimize the Mini App.
  onEvent(eventType: string, eventHandler: (event: any) => void): void; // Sets the app event handler.
  offEvent(eventType: string, eventHandler: (event: any) => void): void; // Deletes a previously set event handler.
  sendData(data: string): void; // Sends data to the bot and closes the Mini App.
  switchInlineQuery(query: string, chooseChatTypes?: string[]): void; // Inserts the bot's username and the specified inline query in the current chat's input field.
  openLink(url: string, options?: { try_instant_view?: boolean }): void; // Opens a link in an external browser.
  openTelegramLink(url: string): void; // Opens a Telegram link inside the Telegram app.
  openInvoice(url: string, callback?: (status: string) => void): void; // Opens an invoice using the link.
  shareToStory(mediaUrl: string, params?: StoryShareParams): void; // Opens the native story editor with the media specified.
  showPopup(params: PopupParams, callback?: (id: number) => void): void; // Shows a native popup.
  showAlert(message: string, callback?: () => void): void; // Shows a simple alert with a 'Close' button.
  showConfirm(message: string, callback?: (confirmed: boolean) => void): void; // Shows a simple confirmation window with 'OK' and 'Cancel' buttons.
  showScanQrPopup(params: ScanQrPopupParams, callback?: (text: string) => boolean): void; // Shows a native popup for scanning a QR code.
  closeScanQrPopup(): void; // Closes the native popup for scanning a QR code.
  readTextFromClipboard(callback?: (text: string) => void): void; // Requests text from the clipboard.
  requestWriteAccess(callback?: (granted: boolean) => void): void; // Requests permission to send messages to the user.
  requestContact(callback?: (shared: boolean) => void): void; // Prompts the user for their phone number.
  ready(): void; // Informs the Telegram app that the Mini App is ready to be displayed.
  expand(): void; // Expands the Mini App to the maximum available height.
  close(): void; // Closes the Mini App.
  enableClosingConfirmation(): void
}
