import { Pressable, Text, PressableProps } from 'react-native';
import { styles } from '../theme';

export function ActionButton({ label, variant = 'primary', ...props }: PressableProps & { label: string; variant?: 'primary' | 'outline' }) {
  return <Pressable {...props} style={state => [variant === 'primary' ? styles.primary : styles.outline, typeof props.style === 'function' ? props.style(state) : props.style]}><Text style={variant === 'primary' ? styles.primaryText : styles.outlineText}>{label}</Text></Pressable>;
}
