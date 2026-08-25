import { TextInput, TextInputProps } from 'react-native';
import { styles } from '../theme';

export function Field(props: TextInputProps) {
  return <TextInput {...props} style={[styles.input, props.style]} placeholderTextColor="#9a9c93" />;
}
